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

public class TestRabbitMQ_3_publish {

	//发布订阅模式
	private Connection connection = null;
	
	//定义rabbit连接池
	@Before
	public void initConnection() throws IOException{
		//定义工厂对象
		ConnectionFactory connectionFactory = new ConnectionFactory();
		//设定参数
		connectionFactory.setHost("192.168.126.146");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/jt");
		connectionFactory.setUsername("jtadmin");
		connectionFactory.setPassword("jtadmin");
		
		//创建连接
		connection = connectionFactory.newConnection();	
	}
	
	
	
	//定义生产者   生成者不会直接链接队列
	@Test
	public void  proverder() throws IOException{
		//定义通道
		Channel channel = connection.createChannel();
		
		//定义交换机名称
		String exchange_name = "E1";
		
		
		//定义发布订阅模式 
		/**
		 * exchange_name:交换机的名称
		 *  type : fanout表示发布订阅模式    redirect 路由模式   topic 主题模式
		 */ 
		channel.exchangeDeclare(exchange_name, "fanout");
		
		for(int i=0;i<10; i++){
			String msg = "发布订阅模式"+i;
			channel.basicPublish(exchange_name, "", null, msg.getBytes());
		}
		
		channel.close();
		connection.close();
	}
	
	
	/**
	 * 消费者需要定义队列名称  并且与交换机绑定
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	@Test
	public void consumer1() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		Channel channel = connection.createChannel();
		
		String exchange_name = "E1";  //订阅了生成者
		String queue_name = "c_1";
		
		//定义交换机模式
		channel.exchangeDeclare(exchange_name, "fanout");
		
		//定义队列
		channel.queueDeclare(queue_name, false, false, false, null);
		
		//将队列和交换机绑定   String 路由key
		channel.queueBind(queue_name, exchange_name, "");
		
		//定义消费数量 
		channel.basicQos(1);
		
		//定义消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//将消费者和队列绑定,并且需要手动返回
		channel.basicConsume(queue_name, false, consumer);
		
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			
			String msg = "发布订阅模式-消费者1"+new String(delivery.getBody());
			System.out.println(msg);
			
			//false表示一个一个返回
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
	
	
	/**
	 * 消费者需要定义队列名称  并且与交换机绑定
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	@Test
	public void consumer2() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		Channel channel = connection.createChannel();
		
		String exchange_name = "E1";
		String queue_name = "c_2";
		
		//定义交换机模式
		channel.exchangeDeclare(exchange_name, "fanout");
		
		//定义队列
		channel.queueDeclare(queue_name, false, false, false, null);
		
		//将队列和交换机绑定
		channel.queueBind(queue_name, exchange_name, "");
		
		channel.basicQos(1);
		
		//定义消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//定义回复方式
		channel.basicConsume(queue_name, false, consumer);
		
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			
			String msg = "发布订阅模式-消费者2"+new String(delivery.getBody());
			System.out.println(msg);
			
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}

	}
	
	
	
}
