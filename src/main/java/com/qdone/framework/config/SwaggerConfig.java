package com.qdone.framework.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.qdone"})//必须存在
/*@ConditionalOnExpression("${swagger.open:true}")*/
public class SwaggerConfig{
	
	 @Value("${swagger.open:true}")
	 private Boolean isOpen;
    
    /*添加自定义异常信息*/
	private ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>() {
		private static final long serialVersionUID = 1L;
		{
			add(new ResponseMessageBuilder().code(200).message("请求成功").build());
			add(new ResponseMessageBuilder().code(400).message("请求参数错误").responseModel(new ModelRef("Error")).build());
			add(new ResponseMessageBuilder().code(401).message("权限认证失败").responseModel(new ModelRef("Error")).build());
			add(new ResponseMessageBuilder().code(404).message("请求资源不存在").responseModel(new ModelRef("Error")).build());
			add(new ResponseMessageBuilder().code(405).message("请求方式不支持").responseModel(new ModelRef("Error")).build());
			add(new ResponseMessageBuilder().code(500).message("服务器内部错误").responseModel(new ModelRef("Error")).build());
		}
	};
	
	/**
	 * swagger分组InnerApi
	 */
    @Bean
    public Docket innerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.enable(isOpen)
                .groupName("InnerApi")  
                .genericModelSubstitutes(DeferredResult.class)  
                .useDefaultResponseMessages(false)  
                .forCodeGeneration(true)  
                .select()  
                .apis(RequestHandlerSelectors.basePackage("com.qdone.module.app"))
                .paths(PathSelectors.any())
                .build()  
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                .apiInfo(innerApiInfo());
    }
    
    /**
     * swagger分组InnerApi
     * 配置文档说明信息
     * @return
     */
    private ApiInfo innerApiInfo() {
	        Contact contact = new Contact("付为地", "", "1335157415@qq.com");
	        StringBuffer sb=new StringBuffer(1024);
	        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Swagger</strong>是一款RESTFUL接口的文档在线自动生成+功能测试功能软件,用于生成、描述、调用和可视化 RESTful风格的 Web服务。<br>总体目标是使客户端和文件系统作为服务器以同样的速度来更新,文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。<br>");
	        sb.append("<strong>注意事项:</strong><ul><br>");
	        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>1.本接口文档,如果没有特殊限定,默认仅支持<strong>POST</strong>方式。<br></li>");
	        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>2.参数描述<strong>模型</strong>和<strong>示例</strong>,分别针对请求的<strong>参数结构</strong>和<strong>参数示例</strong>进行描述。<br></li>");
	        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>3.响应描述<strong>模型</strong>和<strong>示例</strong>,分别针对响应的<strong>数据结构</strong>和<strong>数据示例</strong>进行描述。<br></li></ul>");
	        return new ApiInfoBuilder()
	                .title("API内部接口")
	                .description(sb.toString())
	                .contact(contact)
	                .version("1.1.0")
	                .build();
    }



	/**
	 * swagger分组OpenApi
	 * @return
	 */
	@Bean
	public Docket customDocket() {
		//自定义异常信息
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(isOpen)
				.groupName("OpenApi")
				.genericModelSubstitutes(DeferredResult.class)
				.useDefaultResponseMessages(false)
				.forCodeGeneration(true)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.qdone.module.controller"))
				.paths(PathSelectors.any())
				.build()
				.globalResponseMessage(RequestMethod.GET, responseMessages)
				.globalResponseMessage(RequestMethod.POST, responseMessages)
				.globalResponseMessage(RequestMethod.PUT, responseMessages)
				.globalResponseMessage(RequestMethod.DELETE, responseMessages)
				.globalResponseMessage(RequestMethod.PATCH, responseMessages)
				.apiInfo(apiInfo());
	}
	/**
	 * swagger分组OpenApi
	 * 配置文档说明信息
	 * @return
	 */
	private ApiInfo apiInfo() {
		Contact contact = new Contact("付为地", "", "1335157415@qq.com");
		StringBuffer sb=new StringBuffer(1024);
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Swagger</strong>是一款RESTFUL接口的文档在线自动生成+功能测试功能软件,用于生成、描述、调用和可视化 RESTful风格的 Web服务。<br>总体目标是使客户端和文件系统作为服务器以同样的速度来更新,文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。<br>");
		sb.append("<strong>注意事项:</strong><ul><br>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>1.本接口文档,如果没有特殊限定,默认仅支持<strong>POST</strong>方式。<br></li>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>2.参数描述<strong>模型</strong>和<strong>示例</strong>,分别针对请求的<strong>参数结构</strong>和<strong>参数示例</strong>进行描述。<br></li>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<li>3.响应描述<strong>模型</strong>和<strong>示例</strong>,分别针对响应的<strong>数据结构</strong>和<strong>数据示例</strong>进行描述。<br></li></ul>");
		return new ApiInfoBuilder()
				.title("API开放接口")
				.description(sb.toString())
				.contact(contact)
				.version("1.1.0")
				.build();
	}

}