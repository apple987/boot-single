package com.qdone.common.job;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.qdone.common.util.JwtUtils;
import com.qdone.module.model.TokenRequestHistory;
/**
 * @author 付为地
 *  简单定时任务
 *  清除jwtUtils.AppTokenRequestHistoryPrefix+token
 *   针对key-url，最大请求时间跟当前时刻间隔超过1小时，直接清除对应的url,访问记录
 *   key:jwtUtils.AppTokenRequestHistoryPrefix+token
 *   value:map
 *            key-url
 *            value-List<TokenRequestHistory>
 */
public class SpringSimpleJob implements SimpleJob {
	
    @Autowired
    private JwtUtils jwtUtils;
    
    @SuppressWarnings("unchecked")
	@Override
    public void execute(final ShardingContext context) {
    	System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
    			context.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));
        String shardParamter = context.getShardingParameter();
        System.out.println("分片参数："+shardParamter);
        TreeSet<String> keys=jwtUtils.keys(jwtUtils.AppTokenRequestHistoryPrefix+"*");
        Iterator<String> it=keys.iterator();
        while(it.hasNext()){
	        	String tokenKey=it.next();
	           long rateTokenRemain= jwtUtils.getRateTokenRemainTime(tokenKey);//剩余存活时间秒数
	        	Map<String,Object> history=jwtUtils.get(tokenKey.getBytes(), ConcurrentHashMap.class);
	        	if(!ObjectUtils.isEmpty(history)){
		        	Iterator<Map.Entry<String, Object>> urlKeys=history.entrySet().iterator();
		        	long now=System.currentTimeMillis();//取出tokenKey对应时间戳
		        	int  initTokenKeySize=history.keySet().size();//初始key的长度,判断本次针对tokenKey是否需要更新内容
		        	while(urlKeys.hasNext()){
			        		Map.Entry<String, Object> me=urlKeys.next();
			        		String url=me.getKey();
			        		List<TokenRequestHistory> urlHistory=(List<TokenRequestHistory>) me.getValue();
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
								      	      //当前时间戳跟最近时刻比较，超过1小时(含)，直接删除url记录
								      	   	long max=urlHistory.get(0).getTime();
								      	     if(now-max>=1000*60*60){
								      	       	       history.remove(url);
								      	      }
					          }
			       	}
		        	//判断history存储内容是否发生变化
		        	if(initTokenKeySize!=history.keySet().size()){
		        		jwtUtils.set((tokenKey).getBytes(), (int)rateTokenRemain, history);
		        	}
	        	}
        }
    }

    
   
}
