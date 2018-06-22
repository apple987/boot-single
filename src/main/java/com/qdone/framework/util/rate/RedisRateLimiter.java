package com.qdone.framework.util.rate;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;

/**
 * 接口限流器
 *
 */
public class RedisRateLimiter{
	
    /*初始化配置redisson，限流key*/
    public  final static String REDISSON_RATE_LIMITER_KEY = "REDISSON_RATE_LIMITER:";//redisson默认限流key
    public  final  static int REDISSON_RATE_LIMITER_PERMITS = 1000;//redisson默认限流次数
    public  final  static long REDISSON_RATE_LIMITER_TIMEOUT = 1L;//redisson默认限流超时数
    public  final static TimeUnit REDISSON_RATE_LIMITER_TIMEUNIt = TimeUnit.SECONDS;//redisson默认有效期单位
	
     /**
	   * 采用redisson方式，
	   *   自定义限流器
	   *   该方法可以配合 mq，结果是 true 的话就 ack，false 的话就 reject
	   * @param redisClient
	   * @param rateKey：自定义key
	   * @param permits:允许最大限流数
	   * @param timeout:时间间隔单位秒
	   * @param timeUnit:时间间隔类型，默认秒
	  */
	  public static boolean acquire(RedissonClient redisClient,String rateKey,long permits,long timeout,TimeUnit timeUnit) {
		    RMapCache<String, Integer> msgRateLimit =redisClient.getMapCache(rateKey, IntegerCodec.INSTANCE);
		    msgRateLimit.putIfAbsent(rateKey, 0, timeout, timeUnit);
		    Integer tokenCount = msgRateLimit.addAndGet(rateKey, 1);//目前总占用额度数
		    return tokenCount>permits?false:true;
	  }
	  
	  /**
	   * 直接使用redisson限流器
	   * 参考:https://github.com/redisson/redisson/wiki
	   *   限流器redisson3.7.1版本
	   *   本处默认配置都是秒，可以手动修改
	   *   重启服务，可以手动清除一下redis库 
	   * @param redisClient
	   * @param rateKey：自定义key
	   * @param permits:允许最大限流数
	   * @param timeout:时间间隔单位秒
	   * @param timeUnit:时间间隔类型，默认秒
	   */
	  public static boolean tryAcquire(RedissonClient redisClient,String rateKey,long permits,long timeout,TimeUnit timeUnit) {
		    RRateLimiter rateLimiter = redisClient.getRateLimiter(rateKey);
		    //初始化,最大流速 = 每1秒钟产生10个令牌
		    /*rateLimiter.trySetRate(RateType.OVERALL, 1000, 1, RateIntervalUnit.SECONDS);//1秒1000个token*/
		    rateLimiter.trySetRate(RateType.OVERALL, permits, timeout, RateIntervalUnit.SECONDS);//1秒1000个token
		    return rateLimiter.tryAcquire(1,0, TimeUnit.SECONDS);//间隔一秒，尝试一次，失败就不重试，直接拒绝访问，0换成正数，就会间隔重试
	  }
}
