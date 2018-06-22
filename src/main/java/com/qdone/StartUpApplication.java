package com.qdone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动器 
 *  Session集群方案
 *   1.@EnableRedisHttpSession+spring-session-data-redis:配置session采用redis同步
 *   2.@EnableRedissonHttpSession+spring-session:配置redisson同步session
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableRedisHttpSession
public class StartUpApplication implements CommandLineRunner{

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(StartUpApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("系统资源初始化完成！！！！！！");
	}

}
