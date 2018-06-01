package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.DubboCartService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired	//代理对象
	private DubboCartService dubboCartService;
	
	// /cart/show.html
	@RequestMapping("/show")
	public String show(Model model){
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = dubboCartService.findCartListById(userId);
		model.addAttribute("cartList", cartList);
		//转向页面
		return "cart";
	}
	
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(@PathVariable Long itemId,
			@PathVariable Integer num){
		try {
			Long userId = UserThreadLocal.get().getId();
			Cart cart = new Cart();
			cart.setUserId(userId);
			cart.setItemId(itemId);
			cart.setNum(num);
			dubboCartService.updateCartNum(cart);
			System.out.println("程序调用成功");
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"购物车修改失败");
	}
	
	
	//实现购物车删除操作
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId){
		Long userId = UserThreadLocal.get().getId();
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		dubboCartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
	
	//实现购物车新增 
	@RequestMapping("/add/{itemId}")
	public String saveCart(@PathVariable Long itemId,Cart cart){
		
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		dubboCartService.saveCart(cart);
		//跳转到购物车展现页面
		return "redirect:/cart/show.html";
	}
	
	
	
	
	
	
	
}
