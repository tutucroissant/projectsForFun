package com.jt.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	/**
	 * 问题1.如何返回true或false
	 * 解决方案: 通过查询数据库中的记录数 0或者非0 实现true和false返回
	 * 
	 * 问题2:如何根据需求实现 1 表示查询username  2表示查询phone
	 * 解决方案: 通过分支结构 完美的处理
	 * 
	 */
	@Override
	public Boolean findCheckUser(String param, Integer type) {
		//定义列的名称
		String  cloumn = null;
		
		switch (type) {
		case 1:
			cloumn = "username"; break;
		case 2:
			cloumn = "phone";	 break;
		case 3:
			cloumn = "email";	 break;
		}
		int count = userMapper.findCheckUser(param,cloumn);
		//true 代表用户以存在,false表示不存在
		return count==0 ? false : true;
	}
	
	@Override
	public String saveUser(User user) {
		//为user补全数据
		String md5Password = 
				DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Password);
		user.setEmail(user.getPhone());
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		userMapper.insert(user);

		return user.getUsername();
	}

	/**
	 * 1.根据用户名和密码查询信息
	 * 2.如果查询的数据不为null.表示用户名和密码正确
	 * 3.形成加密的ticket
	 * 4.将用户信息转化为JSON串.
	 * 5.将ticket:json 存入redis中
	 * 6.返回ticket
	 * 
	 */
	@Override
	public String findUserByUP(User user) {
		
		//1.将密码加密
		String md5Password = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5Password);
		
		//2.根据用户名和密码查询
		User userDB = userMapper.findUserByUP(user);
		
		String ticket = null;
		if(userDB != null){
			try {
				String userJSON = objectMapper.writeValueAsString(userDB);
				ticket = DigestUtils.md5Hex("JT_TICKET_"+ System.currentTimeMillis()+user.getUsername());
				
				//将数据写入redis中
				//jedisCluster.set(ticket, userJSON, "NX", "EX", 60*60*48);
				jedisCluster.setex(ticket, 60*60*48, userJSON);
				//jedisCluster.set(ticket,userJSON);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ticket;
	}
}
