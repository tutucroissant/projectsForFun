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

public class TestRabbitMQ {
	
	/**
	 * 1.通过对象获取Connection连接
	 * 		1.1注入ip/端口/用户名/密码/虚拟主机
	 */
	
	private Connection connection = null;
	String queueName = "simple";
	
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
	
	//定义提供者
	@Test
	public void provider() throws IOException{
		
		//1.创建channel对象  管理队列/生产者/消费者
		Channel channel = connection.createChannel();
		
		//2.定义队列的名称
		String queueName = "simple";
		
		//3.创建对象队列
		/**
		 * 参数介绍
		 * 1.queue 队列的名称
		 * 2.durable 是否持久化 如果队列中有消息那么当队列重启时,队列
		 * 	中的消息是否恢复   true/false
		 * 3.exclusive  表示是否为提供者独有,如果设置为true则消费者不能消费
		 * 4.autoDelete 如果消息队列中没有消息是否自动删除 false
		 * 5.arguments  是否传递参数  如果没有参数添加null
		 */
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		//4.准备需要发送的消息
		String msg = "我是简单模式";
		
		//5.将消息发送到消息队列中
		/**
		 * exchange:表示交换机  将消息发往不同的队列中  如果没有添加""空串
		 * routingKey:路由key 根据指定的路由key将消息发往特定的队列中.
		 * 			  如果没有路由key则使用队列名称代替
		 * 
		 * props:第三方的配置文件 如果没有使用null
		 * body: 消息转化后的字节码数组
		 */
		channel.basicPublish("", queueName, null, msg.getBytes());
		
		//6.关闭通道和连接
		channel.close();
		System.out.println("程序执行成功!!!");
	}
	
	/**
	 * 1.创建channel对象
	 * 2.创建队列
	 * 3.创建消费者对象
	 * 4.从对象中获取信息
	 * 5.信息输出
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	@Test
	public void consumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(queueName, true, false, false, null);
		//创建消费者对象
		QueueingConsumer consumer = 
				new QueueingConsumer(channel);
		/**
		 * 将消费者和队列进行绑定   
		 * 1.queue:队列的名称
		 * 2.autoAck   rabbitMQ中的返回信息  true表示自动回复   false需要手动回复
		 * 	 由于网络原因可能会造成回复不及时. 最大允许3次没有回复.
		 * 3.callback 表示回调 消息最终发送给谁.
		 */
		channel.basicConsume(queueName, true, consumer);
		//通过循环方式 实现队列的监听
		while(true){
			//delivery中包含了消息
			QueueingConsumer.Delivery delivery = 
					consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("获取队列消息成功!!!!"+msg);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
