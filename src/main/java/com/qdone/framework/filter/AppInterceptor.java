package com.qdone.framework.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qdone.common.util.JwtUtils;
import com.qdone.framework.annotation.Login;
import com.qdone.framework.core.constant.Constants;
import com.qdone.framework.exception.RRException;
import com.qdone.module.model.TokenRequestHistory;

import io.jsonwebtoken.Claims;

/**
 * @author 付为地
 *    APP登录token验证，需要登录的路径，必须同时传递token和userId
 *  防止A客户模拟B客户，进行系统操作
 */

@Component
@SuppressWarnings("unchecked")
public class AppInterceptor extends HandlerInterceptorAdapter {
	
    @Autowired
    private JwtUtils jwtUtils;

    public static final String USER_KEY = "userId";

   
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	request.setCharacterEncoding(Constants.CHARSET.UTF8);
    	response.setCharacterEncoding(Constants.CHARSET.UTF8);
    	boolean isCheck=true;//是否限流检测
    	Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }
        if(annotation == null){
            return true;
        }
        //判断是否支持限流
        isCheck=annotation.isCheck();
        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }
        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new RRException(jwtUtils.getHeader() + ",不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        //获取用户编号
        String userId =StringUtils.isBlank(request.getHeader(USER_KEY))?request.getParameter(USER_KEY):request.getHeader(USER_KEY);
        if(StringUtils.isBlank(userId)){
        	throw new RRException(USER_KEY + ",不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        //验证token时效
       /* 
                    方案一：采用jwttoken验证失效时间，本处未启用，想使用此方式的朋友，请自行打开对应代码
        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(token)
            &&claims!=null&&!jwtUtils.isTokenExpired(claims.getExpiration())){
        	if(!jwtUtils.exists(jwtUtils.AppTokenPrefix+claims.getSubject())){
        		throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        	}
            if(!userId.equals(claims.getSubject())){
            	throw new RRException(jwtUtils.getHeader() + "非法,请确保本人操作", HttpStatus.UNAUTHORIZED.value());
            }
        }
        */
        
        /*
         * 方案二，token自动续期策略,半小时内自动续期，长时间不操作的，直接过期重新去登陆
         */
        long remain=jwtUtils.getExpire();
        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null||StringUtils.isEmpty(claims.getSubject())){
        	 throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }else{
        	remain=jwtUtils.getTokenRemainTime(claims.getSubject());
        	if(remain==-1||remain==-2){//针对token存在，但是没有设置失效时间(-1)，也认为不合法，必须设置失效时间
                throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
            }
        }
        /*验证本人使用*/
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(token)
        	&&claims!=null&&!StringUtils.isEmpty(claims.getSubject())&&remain>0){
            if(!userId.equals(claims.getSubject())){
            	throw new RRException(jwtUtils.getHeader() + "非法,请确保本人操作", HttpStatus.UNAUTHORIZED.value());
            }
        }
        /*方案一，token自动续期策略，重新生成新token使用，不考虑强制替换cookie内存储的旧token,半小时内重新生成新token*/
       /* long remain=claims.getExpiration().getTime()-new Date().getTime();
        if(remain<=1800000){
        	String freshToken=jwtUtils.refreshTokenExpiration(token);
        	request.setAttribute("freshToken", freshToken);
        	Result<User> resp=new Result<User>();
        	resp.setCode(200);
        	resp.setBizCode(401);
        	resp.setMsg("令牌失效,请使用新令牌请求!");
        	resp.setData(new User(claims.getSubject(),token,freshToken));
        	PrintWriter pw=response.getWriter();
        	pw.write(JSON.toJSONString(resp));
        	pw.close();
        	return false;
        }*/
        /*方案二，token自动续期策略,半小时内自动续期，长时间不操作的，直接过期重新去登陆*/
        if(remain>0&&remain<=1800){//快过期30分钟(含)以内,自动续期token过期时间
        	jwtUtils.refreshToken(token);
        }
        //设置userId到request里，后续根据userId，获取用户信息
        /*request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));*/
        
        /**
         * 限制接口访问频次，新增逻辑
         * 查看接口请求历史记录，
         * 针对相同接口，频繁多次请求
         * jwtUtils.AppTokenRequestHistoryPrefix+token不需要考虑自动续期，postHandle里面已经重新设置value和有效期
         * 本次接口如果已经超限，直接拒绝访问
         *  存储格式
         *          key:jwtUtils.AppTokenRequestHistoryPrefix+token
         *          value:map
         *                   key-url
         *                   value-List<TokenRequestHistory>
         *        
         */
        //限流检测，不开启限流，就不会检查，以前如果存储的有，token访问记录，本次处理
        if(isCheck&&StringUtils.isNotBlank(token)&&jwtUtils.exists(jwtUtils.AppTokenRequestHistoryPrefix+token)){
        	String url=request.getRequestURI();
        	Map<String,Object> history=jwtUtils.get((jwtUtils.AppTokenRequestHistoryPrefix+token).getBytes(), ConcurrentHashMap.class);
        	if(history.containsKey(url)){
        		List<TokenRequestHistory> urlHistory=(List<TokenRequestHistory>) history.get(url);
        		if(!CollectionUtils.isEmpty(urlHistory)){
		   	       		 //访问时间降序排列 
		   	       		 Collections.sort(urlHistory, new Comparator<TokenRequestHistory>() {
		   	       			     @Override  
		   	    	             public int compare(TokenRequestHistory t1, TokenRequestHistory t2) {  
		   	    	            	if(t1.getTime()>t2.getTime()){
		   	    	            		return -1;
		   	    	            	}else if(t1.getTime()==t2.getTime()){
		   	    	            		return 0;
		   	    	            	}else{
		   	    	            		return 1;
		   	    	            	}
		   	    	            }  
		   	       	     }); 
			   	      	TokenRequestHistory max=urlHistory.get(0);//取出距离本次请求，最近那次请求记录
			           	long now=System.currentTimeMillis();
			           	if(url.equals(max.getUrl())&&(now-max.getTime()<=5000)){//间隔超过5秒(含)
			           		throw new RRException("您请求的太过频繁了,请休息一下,稍后重试！", HttpStatus.SERVICE_UNAVAILABLE.value());
			           	}
          		 }
        	}
        }
        return true;
    }
    
    /*
     *  记录请求接口时间戳
     *  过于频繁操作接口，
     *  preHandle考虑加入过滤频繁操作接口
     */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
    	if(handler instanceof HandlerMethod) {//针对Login注解方法操作
    		HandlerMethod handlerMethod = (HandlerMethod) handler;
    		//获取用户凭证
    		String token = request.getHeader(jwtUtils.getHeader());
    		if(StringUtils.isBlank(token)){
    			token = request.getParameter(jwtUtils.getHeader());
    		}
        	if(handlerMethod.hasMethodAnnotation(Login.class)&&!StringUtils.isEmpty(token)){
        		Login login=handlerMethod.getMethodAnnotation(Login.class);
        		/*1.开启限流记录接口请求历史 2.不开启限流，自定义指定记录接口请求历史
        		 * 注意：A.(默认)正常情况下开启限流 isCheck，一般都会自动isSave保存(isCheck=true,isSave=true)
        		 *     B.如果只想验证一次，不想更新接口请求历史记录(isCheck=true,isSave=false)
        		 *     C.如果不想判断限流，只想保存接口请求记录(isCheck=false,isSave=true)
        		 *   两者组合使用，看情况，支持您自己设定
        		 if(login.isCheck()||(!login.isCheck()&&login.isSave())){*/
        		/*本次记录更新接口请求历史记录*/
        		if(login.isSave()){	
                    String url=request.getRequestURI();
                    long time=System.currentTimeMillis();
                    String className=handlerMethod.getBeanType().getName();
                    Method method = handlerMethod.getMethod();
                    String methodName=method.getName();
                    String functionName=className+"."+methodName;
                    TokenRequestHistory TokenRequest=new TokenRequestHistory(token,url,className,methodName,functionName,time);
                    /**
                     * 存储格式
                     *  key:jwtUtils.AppTokenRequestHistoryPrefix+token
                     *  value:map
                     *          key-url
                     *          value-urlHistory
                     */
                    //存储jwtUtils.AppTokenRequestHistoryPrefix+token对应请求url的请求最近5次数历史记录
                    Map<String,Object> history=new ConcurrentHashMap<String,Object>();
                    //默认记录相同url路径的5条最近记录
                    List<TokenRequestHistory> urlHistory=new ArrayList<TokenRequestHistory>(5);
                    //以前如果存储的有，token访问记录，本次处理
    		            if(jwtUtils.exists(jwtUtils.AppTokenRequestHistoryPrefix+token)){
    		                	history=jwtUtils.get((jwtUtils.AppTokenRequestHistoryPrefix+token).getBytes(), ConcurrentHashMap.class);
    		                	if(history.containsKey(url)){
    		                		urlHistory=(List<TokenRequestHistory>) history.get(url);
    		                		 if(!CollectionUtils.isEmpty(urlHistory)){
    			                		 //访问时间降序排列 
    			                		 Collections.sort(urlHistory, new Comparator<TokenRequestHistory>() {
    			                			 @Override  
    			             	             public int compare(TokenRequestHistory t1, TokenRequestHistory t2) {  
    			             	            	if(t1.getTime()>t2.getTime()){
    			             	            		return -1;
    			             	            	}else if(t1.getTime()==t2.getTime()){
    			             	            		return 0;
    			             	            	}else{
    			             	            		return 1;
    			             	            	}
    			             	            }  
    			                	     });  
    			                		//保证只存储相同接口的5条最近的请求历史记录
    			                		if(urlHistory.size()>=5){
    			                			urlHistory=urlHistory.subList(0, 5);
    			                			urlHistory.set(5, TokenRequest);//修改距离本次最远的第5条记录
    			                		}else{
    			                			urlHistory.add(TokenRequest);//不到五条，直接装
    			                		}
    			                    }else{//相同接口以前没有存储过，直接存入
    			                    	urlHistory.add(TokenRequest);
    			                    }
    		                }else{//以前没有存储过，直接存入
    		                	urlHistory.add(TokenRequest);
    		                }
    		                history.put(url, urlHistory);
    		         }else{
    		        	 urlHistory.add(TokenRequest);
    		        	 history.put(url, urlHistory);
    		         }
                    //存储登陆记录默认存储7天
                    jwtUtils.set((jwtUtils.AppTokenRequestHistoryPrefix+token).getBytes(), jwtUtils.getExpire(), history);
        		}
        	}
        }
	}
}
