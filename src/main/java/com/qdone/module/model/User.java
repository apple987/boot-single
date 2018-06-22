package com.qdone.module.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户对象,添加swagger属性描述支持
 * 参考资料:https://jakubstas.com/spring-jersey-swagger-create-documentation/
 */
@ApiModel(value = "用户对象", description = "用户实体类")
public class User implements Serializable{

	private static final long serialVersionUID = -986960169531607635L;
	
	@ApiModelProperty(value = "姓名", required = true)
    @NotEmpty(message = "姓名不能为空")
	private String name;
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	@ApiModelProperty(value = "性别",hidden=true)
	private Integer sex;
	@ApiModelProperty(value = "年龄", required = true)
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄大于 0")
    @Max(value = 300, message = "年龄不大于 300")
	private Integer age;
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	@ApiModelProperty(value = "新令牌", required = true)
	private String freshToken;

	public User() {
		super();
	}
	
	public User(@NotEmpty(message = "姓名不能为空") String name, String token, String freshToken) {
		super();
		this.name = name;
		this.token = token;
		this.freshToken = freshToken;
	}


	public User(String name, String password, Integer sex, String token) {
		super();
		this.name = name;
		this.password = password;
		this.sex = sex;
		this.token = token;
	}

	public User(@NotEmpty(message = "姓名不能为空") String name, String password, Integer sex,
			@NotNull(message = "年龄不能为空") @Min(value = 0, message = "年龄大于 0") @Max(value = 300, message = "年龄不大于 300") Integer age,
			String token, String freshToken) {
		super();
		this.name = name;
		this.password = password;
		this.sex = sex;
		this.age = age;
		this.token = token;
		this.freshToken = freshToken;
	}

	public String getFreshToken() {
		return freshToken;
	}

	public void setFreshToken(String freshToken) {
		this.freshToken = freshToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
