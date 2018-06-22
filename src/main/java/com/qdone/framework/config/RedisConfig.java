package com.qdone.framework.config;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qdone.framework.util.RedisCache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 配置redis缓存
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{
	

	
	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private int port;
	
	@Value("${spring.redis.password}")
	private String password;
	
	
	@Bean
	public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

	/**
	 * 自定义缓存策略
	 */
	@SuppressWarnings({ "rawtypes"})
	@Bean
    public SimpleCacheManager simpleCacheManager(RedisTemplate redisTemplate) {
		    SimpleCacheManager simple = new SimpleCacheManager();
		    Set<RedisCache> data=new HashSet<RedisCache>();
		    data.add(new RedisCache(redisTemplate,"view",60));
		    data.add(new RedisCache(redisTemplate,"defaultCache",1800));
		    simple.setCaches(data);
	        return simple;
	 }
	/**
	 * 定义redisTemplate
	 *  可以定义多个RedisTemplate,
	 *   本处强制全部使用集群版本RedisTemplate
	 */
	@Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        template.setEnableTransactionSupport(true);//开启事务支持
        return template;
    }
	
	
	/**
	 * 集群配置redssion
	 */
	@Bean
	public RedissonClient createRedission() {
		String address="redis://"+host+":"+port;
		Config config = new Config();
		if(StringUtils.isNotEmpty(password)){
			config.useSingleServer().setConnectTimeout(20000)
			.setPassword(password)
			.setAddress(address);
		}else{
			config.useSingleServer().setConnectTimeout(20000)
			.setAddress(address);
		}
		return  Redisson.create(config);
	}
	
	
	/*@Bean(name="redisConnectionFactory")
	@Primary
	@ConditionalOnMissingBean
	public RedisConnectionFactory connectionFactory() {
		RedisStandaloneConfiguration config=new RedisStandaloneConfiguration(host,port);
		if(StringUtils.isNotEmpty(password)){
			config.setPassword(RedisPassword.of(password));
		}
		return new JedisConnectionFactory(config);
	}*/
	
	@Bean
    public JedisPool jedisPool() {
	        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	        jedisPoolConfig.setMaxTotal(100);
	        jedisPoolConfig.setTestOnBorrow(true); 
	        jedisPoolConfig.setMaxWaitMillis(-1);
	        if(StringUtils.isNotEmpty(password)){
	        	return new JedisPool(jedisPoolConfig, host, port, 0, password);
	        }else{
	        	return new JedisPool(jedisPoolConfig, host, port, 0);
	        }
	 }
	
	
	@Bean
    public Jedis jedis() {
	       return jedisPool().getResource();
	 }
	
	
}