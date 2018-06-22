package com.qdone.module.model;

import java.io.Serializable;

/**
 * @author 付为地
 *  记录app请求接口历史参数
 */
public class TokenRequestHistory implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String token;//请求接口使用的token
	private String  url;//请求url
	private String  className;//请求类名称
	private String  methodName;//请求方法名称
	private String  functionName;//方法全地址名称
	private long  time;//请求接口时间戳
	
	public TokenRequestHistory() {
		super();
	}
	public TokenRequestHistory(String token, String url, String className, String methodName, String functionName,
			long time) {
		super();
		this.token = token;
		this.url = url;
		this.className = className;
		this.methodName = methodName;
		this.functionName = functionName;
		this.time = time;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
	

}
