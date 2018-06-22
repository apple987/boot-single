package com.qdone.framework.filter;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qdone.framework.annotation.RateLimiter;
import com.qdone.framework.exception.RRException;
import com.qdone.framework.util.rate.RedisRateLimiter;

/**
 * @author 付为地
 *   接口限流拦截器
 */
@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
	
	private Logger logger = LoggerFactory.getLogger(RateLimiterInterceptor.class);
	
	@Autowired
    private RedissonClient redisClient;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
			if(handler instanceof HandlerMethod){
				 HandlerMethod handlerMethod = (HandlerMethod) handler;
		         Method method = handlerMethod.getMethod();
		         RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
		         if (rateLimiter != null) {
		             int limit = ObjectUtils.isEmpty(rateLimiter.limit())?RedisRateLimiter.REDISSON_RATE_LIMITER_PERMITS:rateLimiter.limit();
		             long timeout = ObjectUtils.isEmpty(rateLimiter.timeout())?RedisRateLimiter.REDISSON_RATE_LIMITER_TIMEOUT:rateLimiter.timeout();
		             String rateKey=StringUtils.isEmpty(rateLimiter.rateKey())?request.getRequestURI():rateLimiter.rateKey();//key为空判断
		             rateKey=RedisRateLimiter.REDISSON_RATE_LIMITER_KEY+rateKey;
		             TimeUnit timeUnit=ObjectUtils.isEmpty(rateLimiter.timeout())?RedisRateLimiter.REDISSON_RATE_LIMITER_TIMEUNIt:rateLimiter.timeUnit();
		             /*Boolean isAllow=RedisRateLimiter.acquire(redisClient, rateKey, limit, timeout, timeUnit);*/
		             Boolean isAllow=RedisRateLimiter.tryAcquire(redisClient, rateKey, limit, timeout, timeUnit);
		             if(!isAllow){
		            	 logger.warn("很抱歉，服务器繁忙，请稍后重试!");
		            	 throw new RRException("服务器繁忙，请稍后重试!", HttpStatus.SERVICE_UNAVAILABLE.value());
		             }
		         }
			}
			return true;
		}
	
	
}
