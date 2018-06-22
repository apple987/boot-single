package com.qdone.framework.annotation;

import java.lang.annotation.*;
/**
 * 限流注解
 */
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5;//放行数量,5个
    int timeout() default 1;//限流时间间隔，默认1秒
    String rateKey() default "";//限流器，自定义key
    TimeUnit timeUnit() default TimeUnit.SECONDS;//限流器，默认限流时间单位
}