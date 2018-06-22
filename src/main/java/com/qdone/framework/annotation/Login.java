package com.qdone.framework.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
	String value() default "";
	//本次是否检测，限流间隔时限
	boolean isCheck() default true;
	//本次是否更新，接口访问记录
	boolean isSave() default true;
 }
