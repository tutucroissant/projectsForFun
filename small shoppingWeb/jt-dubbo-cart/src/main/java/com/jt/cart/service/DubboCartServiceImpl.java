package com.jt.cart.service;



import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.cart.mapper.CartMapper;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.DubboCartService;

public class DubboCartServiceImpl implements DubboCartService{
	
	@Autowired
	private CartMapper cartMapper;
	
	
	
	@Override
	public List<Cart> findCartListById(Long userId) {
		
		Cart cart = new Cart();
		cart.setUserId(userId);
		return cartMapper.select(cart);
	}



	@Override
	public void updateCartNum(Cart cart) {
		
		cartMapper.updateCartNum(cart);
		
	}



	@Override
	public void deleteCart(Cart cart) {
		
		cartMapper.delete(cart);
	}


	/**
	 * 1.如果购物车中有该数据则做数量的修改
	 * 2.如果购物车中没有改数据 则做新增操作
	 */
	@Override
	public void saveCart(Cart cart) {
		//补齐数据
		Cart cartDB = cartMapper.findCartByUI(cart);
		if(cartDB !=null){
			//表示购物车中有数据
			int num = cartDB.getNum() + cart.getNum();
			cartDB.setNum(num);
			cartDB.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}else{
			//购物车中没有改数据
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);		
			}
	}
}
