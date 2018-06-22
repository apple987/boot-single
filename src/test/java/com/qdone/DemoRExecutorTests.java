package com.qdone;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.CronSchedule;
import org.redisson.api.RExecutorService;
import org.redisson.api.RMap;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.qdone.module.controller.HelloController;

/**
 * 简单redisson测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRExecutorTests {

	private MockMvc mockMvc;

	
	@Before
	public void setUp() throws Exception {
		/**
		 * 简单测试controller
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
	}

	
	
	@Test
	public void testHello() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/testJson").accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print());
	}

	/*****************************************线程池调用************************************************************************************************/
	public static class RunnableTask implements Runnable {

		@RInject
	    RedissonClient redisson;
		
        @Override
        public void run() {
        	System.err.println("执行RunnableTask任务！");
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("5", "11");
        }
        
    }
    public static class CallableTask implements Callable<String> {
    	
    	@RInject
	    RedissonClient redisson;
        
        @Override
        public String call() throws Exception {
        	System.err.println("执行CallableTask任务！");
            RMap<String, String> map = redisson.getMap("myMap");
            map.put("1", "2");
            return map.get("3");
        }
    }
	/**
	 * 测试redis线程池调用
	 */
	@Test
	public void testRExecutor() {
		    Config config = new Config();
            config.useClusterServers()
            .setPassword("qdone")
            .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6380", "redis://127.0.0.1:6381", "redis://127.0.0.1:6382", "redis://127.0.0.1:6383", "redis://127.0.0.1:6384");
            RedissonClient redisson = Redisson.create(config);
	        RExecutorService e = redisson.getExecutorService("myExecutor");
	        if(!e.isShutdown()){
	        	e.execute(new RunnableTask());
	        	e.submit(new CallableTask());
	        }
	}
	/*****************************************远程调用************************************************************************************************/
     public interface RemoteInterface {
        Long myMethod(Long value);
     }
     public static class RemoteImpl implements RemoteInterface {
        public RemoteImpl() {
        }
        @Override
        public Long myMethod(Long value) {
            return value*2;
        }
     }
   
     @Test
 	 public void testRemoteService() {
    	 Config config = new Config();
         config.useClusterServers()
         .setPassword("qdone")
         .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6380", "redis://127.0.0.1:6381", "redis://127.0.0.1:6382", "redis://127.0.0.1:6383", "redis://127.0.0.1:6384");
    	 RedissonClient server = Redisson.create(config);
         RedissonClient client = Redisson.create(config);
         try {
        	 //服务端注册10个实例，支持并发10个请求,两者可以分布在不同JVM里面
             server.getRemoteService().register(RemoteInterface.class, new RemoteImpl(),10);
             //客户端调用,两者可以分布在不同JVM里面
             RemoteInterface service = client.getRemoteService().get(RemoteInterface.class);
             System.err.println("远程调用执行结果:"+service.myMethod(21L));;
         } finally {
             client.shutdown();
             server.shutdown();
         }
     }
     /*****************************************定时任务
     * @throws ExecutionException 
     * @throws InterruptedException ************************************************************************************************/
     @Test
 	 public void testRScheduledTask() throws InterruptedException, ExecutionException {
    	 Config config = new Config();
         config.useClusterServers()
         .setPassword("qdone")
         .addNodeAddress("redis://127.0.0.1:6379", "redis://127.0.0.1:6380", "redis://127.0.0.1:6381", "redis://127.0.0.1:6382", "redis://127.0.0.1:6383", "redis://127.0.0.1:6384");
         RedissonClient redisson = Redisson.create(config);

         RScheduledExecutorService e = redisson.getExecutorService("myRScheduledTaskExecutor");
         if(!e.isShutdown()){
        	 e.schedule(new RunnableTask(), 10, TimeUnit.SECONDS);
        	 e.schedule(new RunnableTask(), CronSchedule.of("0/5 * * * * ?"));//每五秒执行一次
        	 e.schedule(new RunnableTask(), CronSchedule.dailyAtHourAndMinute(17, 33));
        	 e.schedule(new RunnableTask(), CronSchedule.weeklyOnDayAndHourAndMinute(12, 4, Calendar.MONDAY, Calendar.FRIDAY));
        	 RScheduledFuture<String> future = e.schedule(new CallableTask(), 1, TimeUnit.SECONDS);
        	 System.err.println("定时任务执行:"+future.get());
         }
     }
}
