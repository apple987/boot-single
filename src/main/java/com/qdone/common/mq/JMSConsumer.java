package com.qdone.common.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消费者 
 */
@Component
public class JMSConsumer {
	
	private final static Logger logger = LoggerFactory.getLogger(JMSConsumer.class);
	
    /**
     * 接收队列消息
     * @param msg
     */
    @JmsListener(destination = "springboot.queue.test",containerFactory="queueListenerContainerFactory")
    public void receiveQueue(String msg) {
        logger.info("接收到队列{springboot.queue.test}的消息：{}",msg);
    }
    
    /*
     * 接收主题消息
     */
    @JmsListener(destination = "springboot.topic.test",containerFactory="topicListenerContainerFactory")
    public void receiveTopic(String text) {
        logger.info("接收到主题{springboot.topic.test}消息：{}",text);
    }
}