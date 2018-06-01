package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private DubboCartService cartService;
	
	@Autowired
	private DubboOrderService orderService;
	
	///order/create.htmls
	@RequestMapping("/create")
	public String create(Model model){
		Long userId = UserThreadLocal.get().getId();
		//根据userId查询购物车信息
		List<Cart> cartList = cartService.findCartListById(userId);
		model.addAttribute("carts", cartList);
		//转向到订单确认页面
		return "order-cart";
	}
	
	//http://www.jt.com/service/order/submit
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		try {
			//获取用户IDss
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			String orderId = orderService.saveOrder(order);
			return SysResult.oK(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"订单提交失败");
	}
	
	
	//http://www.jt.com/order/success.html?id=171526893616734
	@RequestMapping("/success")
	public String success(String id,Model model){
		Order order  = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
	
}
