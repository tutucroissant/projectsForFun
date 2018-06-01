package com.jt.dubbo.service;

import com.jt.dubbo.pojo.Order;

public interface DubboOrderService {

	String saveOrder(Order order);

	Order findOrderById(String id);
	
}
