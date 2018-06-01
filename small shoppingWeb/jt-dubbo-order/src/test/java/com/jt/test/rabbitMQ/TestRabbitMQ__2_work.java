package com.jt.test.rabbitMQ;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class TestRabbitMQ__2_work {
	
	
	private Connection connection = null;
	String queueName = "work";
	
	@Before
	public void init() throws IOException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.126.146");
		factory.setPort(5672);
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		factory.setVirtualHost("/jt");
		connection = factory.newConnection();
	}
	
	@Test
	public void provider() throws IOException{
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		String msg = "我是工作模式";
		
		channel.basicPublish("", queueName, null, msg.getBytes());
		
		channel.close();
		connection.close();
	}
	
	@Test
	public void consumer1() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		//设定每次消费的消息数  每次最大拿一个消息进行处理
		channel.basicQos(1);
		
		//定义消费者对象
		QueueingConsumer consumer1 = new QueueingConsumer(channel);
		//消息者和队列进行绑定    如果为false需要手动返回
		channel.basicConsume(queueName, false, consumer1);
		
		while(true){
			QueueingConsumer.Delivery delivery = 
					consumer1.nextDelivery();
			System.out.println("获取消息1:"+new String(delivery.getBody()));
			
			//手动回复   getDeliveryTag()获取消息的下标位置
			//multiple  是否批量回复  ack
			channel.basicAck
			(delivery.getEnvelope().getDeliveryTag(), true);
		}
	}
	
	
	
	@Test
	public void consumer2() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		//设定每次消费的消息数  每次最大拿一个消息进行处理
		channel.basicQos(1);
		
		//定义消费者对象
		QueueingConsumer consumer1 = new QueueingConsumer(channel);
		
		//消息者和队列进行绑定    如果为false需要手动返回
		channel.basicConsume(queueName, false, consumer1);
		
		
		while(true){
			QueueingConsumer.Delivery delivery = 
					consumer1.nextDelivery();
			
			System.out.println("获取消息2:"+new String(delivery.getBody()));
			
			//手动回复   getDeliveryTag()获取消息的下标位置
			//multiple  是否批量回复  ack
			channel.basicAck
			(delivery.getEnvelope().getDeliveryTag(), true);
		}	
	}
}
