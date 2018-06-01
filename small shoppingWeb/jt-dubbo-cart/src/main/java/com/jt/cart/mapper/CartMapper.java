package com.jt.cart.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.dubbo.pojo.Cart;

public interface CartMapper extends SysMapper<Cart>{
	
	//修改购物车数量
	void updateCartNum(Cart cart);

	Cart findCartByUI(Cart cart);
	
}
