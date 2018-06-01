package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;
@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	
	
	
	

	/**
	 * 三张表入库操作
	 * 1.利用通用Mapper实现 一个一个入库 (简单)
	 * 2.利用sql语句实现批量插入操作
	 * 	如何实现高效率的数据入库:
	 * 	通过sql实现批量的插入操作
	 *  insert into order(指定字段) values(#{xxxx})
	 *  insert into tb_order_shipping(...) values(.....)
	 *  
	 *  insert into tb_order_item values(#{...},#{...}),(),(),()
	 */
	/*@Override
	public String saveOrder(Order order) {
		String orderId = order.getUserId()+""+System.currentTimeMillis();
		
		Date date = new Date();
		//1.实现订单表入库
		order.setOrderId(orderId);
		order.setStatus(1);//表示未支付
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单表入库成功");
		
		//2.实现订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流信息入库成功");
		
		//3.实现订单商品入库
		List<OrderItem> orderItemList = order.getOrderItems();
		
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单商品入库成功");
		return orderId;
	}*/
	
	
	/**
	 * 1.利用通用Mapper可以实现Order对象的获取
	 * 	1.1 orderMapper.select(orderId)
	 *  1.2 orderItemMapper.select(...)
	 *  1.3 将数据进行封装 order.setOrderItems(...)
	 * 2.如何通过mybatis实现关联的查询
	 * 	 
	 */
	@Override
	public Order findOrderById(String orderId) {
		
		return orderMapper.findOrderById(orderId);
	}







	
	//先将数据写入消息队列中
	@Override
	public String saveOrder(Order order) {
		//生成orderId
		String orderId = order.getUserId() + "" + System.currentTimeMillis();
		order.setOrderId(orderId);
		
		//routingKey 定义路由key 将对象进行发送
		rabbitTemplate.convertAndSend("save.order", order);
		
		System.out.println("消息队列发送消息");
		
		return orderId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
