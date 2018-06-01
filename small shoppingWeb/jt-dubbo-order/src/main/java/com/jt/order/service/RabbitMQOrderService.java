package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class RabbitMQOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	
	public void saveOrder(Order order){
		String orderId = order.getOrderId();
		
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
		System.out.println("订单商品入库成功:消息队列执行完成");
	}
}
