package com.qdone.module.app;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.JwtUtils;
/*import com.rainsoft.mvc.controller.Result;
import com.rainsoft.mvc.test.User;*/
import com.qdone.common.util.Result;
import com.qdone.framework.annotation.Login;
import com.qdone.module.model.User;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * APP登录授权
 *  app模块的路径前缀都是app开头，AppInterceptor针对app/**路径拦截处理
 */
@Api(tags = "APP登录",description = "演示服务接口")
@RestController
@RequestMapping("/app")
public class AppLoginController {
	
    @Autowired
    private JwtUtils jwtUtils;
    
    /****方案一，token即将过期，本次生成新token，前端请求当次被拒绝，反馈切换新token重新请求*****/
    /**
     * APP登陆
     *    1.上一次登陆token还有效，本次登陆，重新生成token
     *    2.以前没有登陆系统，本次登陆，重新生成token
     */
    @Deprecated
    @ApiOperation(hidden=true,value = "APP登录", httpMethod = "POST", notes = "手机端登录", response = Result.class)
    @PostMapping("doLogin")
    public Result<HashMap<String, Object>> login(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<HashMap<String, Object>> res=new Result<HashMap<String, Object>>();
        /*
         * 用户登录
         * 1.正常流程实际上，先根据用户名密码，数据库查询符合要求的用户信息
         *   如果存在用户，就走后续逻辑
         *   如果不存在用户，就直接抛出异常信息给前端 
         *  本处为了模拟这个操作，暂时直接new一个对象，存储对应信息 
         */
    	
    	String token = jwtUtils.generateToken(userId);
    	System.err.println("生成token是:"+token);
    	HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        res.setData(map);
        //本次登录成功APP用户信息，存入redis中
        Claims claims =jwtUtils.getClaimByToken(token);
        /*保证两个token在redis和生成时，完全一致的过期，剩余过期时间(秒)*/
        User usr=new User();
    	usr.setName(userId);
    	usr.setPassword(password);
    	usr.setToken(token);
    	usr.setSex(1);
    	usr.setAge(20);
        long remain=jwtUtils.getExpire()-(new Date().getTime()-claims.getIssuedAt().getTime())/1000;
    	jwtUtils.set((jwtUtils.AppTokenPrefix+userId).getBytes(), (int)remain, usr);
        return res;
    }

    /**
     * 输入token和userId
     * @param token
     * @param userId
     * @return
     * 刷新token之后，
     *  本次请求直接不会进入控制器
     *  直接抛出异常
     */
    @Deprecated
    @Login
    @GetMapping("userId")
    @ApiOperation(hidden=true,value = "APP获取登陆用户", httpMethod = "GET", notes = "APP获取登陆用户", response = Result.class)
    public Result<User> userInfo(
    		@ApiParam(required = false, value = "令牌", name = "token") @RequestHeader(value = "token")  String token,
    		@ApiParam(required = false, value = "用户名", name = "userId") @RequestHeader(value = "userId")  String userId,HttpServletRequest request){
    	System.err.println(token);
    	System.err.println("新token:"+request.getAttribute("freshToken"));
   	    Result<User> res=new Result<User>();
        /*本处拦截器已经验证了token和userId的唯一对应关系，所以可以直接去userId*/
    	User usr1=jwtUtils.get((jwtUtils.AppTokenPrefix+userId).getBytes(), User.class);
    	System.err.println("序列化拿到的用户信息是:"+JSON.toJSONString(usr1));
    	if(!ObjectUtils.isEmpty(request.getAttribute("freshToken"))){
    		usr1.setFreshToken(request.getAttribute("freshToken").toString());
    	}
    	res.setData(usr1);
    	return res;
    }
    
   /*************************************第二种，token管理策略******************************************************************************/
    
    /**
     * APP登陆
     *    1.上一次登陆token还有效，本次登陆，重新生成token
     *    2.以前没有登陆系统，本次登陆，重新生成token
     */
    @ApiOperation(value = "APP登录", httpMethod = "POST", notes = "手机端登录", response = Result.class)
    @PostMapping("login")
    public Result<User> userLogin(@ApiParam(required = true, value = "账户名称", name = "userId") @RequestParam(value = "userId") String userId,
    		@ApiParam(required = true, value = "账户密码", name = "password") @RequestParam(value = "password")  String password){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(password), "账户密码不能为空");
    	Result<User> res=new Result<User>();
    	//TODO 根据userId和password查询到用户信息，此处直接模拟创建一个user，真实环境请自行查询数据库
        User usr=new User();
    	usr.setName(userId);
    	usr.setPassword(password);
    	usr.setSex(1);
    	usr.setAge(20);
    	usr.setToken(jwtUtils.generateToken(usr));
    	res.setData(usr);
        return res;
    }

    /**
     * APP获取登陆用户
     * @param token：登陆token
     * @param userId：用户名称
     *     同时传递token和userId,确保是本人操作
     */
    @Login
    @GetMapping("user")
    @ApiOperation(value = "APP获取登陆用户", httpMethod = "GET", notes = "APP获取登陆用户", response = Result.class)
    public Result<User> user(
    		@ApiParam(required = false, value = "令牌", name = "token") @RequestHeader(value = "token")  String token,
    		@ApiParam(required = false, value = "用户名", name = "userId") @RequestHeader(value = "userId")  String userId,HttpServletRequest request){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(token), "登陆令牌不能为空");
   	    Result<User> res=new Result<User>();
        /*本处拦截器已经验证了token和userId的唯一对应关系，所以可以直接去userId*/
    	User usr1=jwtUtils.get((jwtUtils.AppTokenPrefix+userId).getBytes(), User.class);
    	System.err.println("序列化拿到的用户信息是:"+JSON.toJSONString(usr1));
    	if(!ObjectUtils.isEmpty(request.getAttribute("freshToken"))){
    		usr1.setFreshToken(request.getAttribute("freshToken").toString());
    	}
    	res.setData(usr1);
    	return res;
    }
    
    @Login
    @GetMapping("getUser")
    @ApiOperation(value = "getUser获取登陆用户", httpMethod = "GET", notes = "getUser获取登陆用户", response = Result.class)
    public Result<User> getUser(
    		@ApiParam(required = false, value = "令牌", name = "token") @RequestHeader(value = "token")  String token,
    		@ApiParam(required = false, value = "用户名", name = "userId") @RequestHeader(value = "userId")  String userId,HttpServletRequest request){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(token), "登陆令牌不能为空");
   	    Result<User> res=new Result<User>();
        /*本处拦截器已经验证了token和userId的唯一对应关系，所以可以直接去userId*/
    	User usr1=jwtUtils.get((jwtUtils.AppTokenPrefix+userId).getBytes(), User.class);
    	System.err.println("getUser序列化拿到的用户信息是:"+JSON.toJSONString(usr1));
    	if(!ObjectUtils.isEmpty(request.getAttribute("freshToken"))){
    		usr1.setFreshToken(request.getAttribute("freshToken").toString());
    	}
    	res.setData(usr1);
    	return res;
    }
    
    /**
     * 退出登录
     */
    @Login(isCheck=false,isSave=false)
    @GetMapping("logout")
    @ApiOperation(value = "APP退出登录", httpMethod = "GET", notes = "APP退出登录", response = Result.class)
    public Result<User> logout(
    		@ApiParam(required = false, value = "令牌", name = "token") @RequestHeader(value = "token")  String token,
    		@ApiParam(required = false, value = "用户名", name = "userId") @RequestHeader(value = "userId")  String userId){
    	Assert.isTrue(StringUtils.isNotEmpty(userId), "账户名称不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(token), "登陆令牌不能为空");
   	    Result<User> res=new Result<User>();
        /*本处拦截器已经验证了token和userId的唯一对应关系，所以可以直接去userId*/
    	User usr1=jwtUtils.get((jwtUtils.AppTokenPrefix+userId).getBytes(), User.class);
    	jwtUtils.logout(userId);
    	jwtUtils.clearRate(token);
    	res.setCode(200);
    	res.setData(usr1);
    	res.setMsg("退出登录");
    	return res;
    }
    
    
}
