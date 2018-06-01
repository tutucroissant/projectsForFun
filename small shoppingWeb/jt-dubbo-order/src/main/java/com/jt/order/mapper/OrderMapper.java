package com.jt.order.mapper;

import java.util.Date;

import com.jt.common.mapper.SysMapper;
import com.jt.dubbo.pojo.Order;

public interface OrderMapper extends SysMapper<Order>{

	Order findOrderById(String orderId);
	
	//将超时订单修改状态
	void updateStatusByDate(Date agoDate);

}
