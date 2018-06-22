package com.qdone.common.mq;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
/**
 * 生产者 
 */
@Component
public class JMSProducer {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
    /**
     * 向指定队列发送消息
     * @param destination
     * @param message
     */
	public void sendMessage(Destination destination, String message) {
		this.jmsTemplate.convertAndSend(destination, message);
	}
	
	 /**
     * 向指定的topic发布消息
     * @param topic
     * @param msg
     */
    public void publish(Destination topic,String msg) {
    	this.jmsTemplate.send(topic, (MessageCreator) session -> {
		    return session.createTextMessage(msg);
		});
    	
    }
}