package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Cart;

	
public interface DubboCartService {
	//根据userId查询购物车信息
	List<Cart> findCartListById(Long userId);
	//修改商品数量
	void updateCartNum(Cart cart);
	//实现购物车删除
	void deleteCart(Cart cart);
	void saveCart(Cart cart);
}
