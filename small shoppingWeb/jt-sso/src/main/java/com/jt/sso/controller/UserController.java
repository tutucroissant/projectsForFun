package com.jt.sso.controller;

import org.joda.time.DateTime;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.jasperreports.JasperReportsCsvView;

import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	//http://sso.jt.com/user/check/{param}/{type}
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public MappingJacksonValue findCheckUser(@PathVariable String param,
			@PathVariable Integer type,String callback){
		
		MappingJacksonValue jacksonValue = null;
		try {
			//true 用户以存在  false表示用名可以使用
			Boolean flag = userService.findCheckUser(param,type);
			jacksonValue = new MappingJacksonValue(SysResult.oK(flag));
			jacksonValue.setJsonpFunction(callback);
			
			return jacksonValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jacksonValue = new MappingJacksonValue
				(SysResult.build(201,"查询失败"));
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue ;
	}
	
	
	//http://sso.jt.com/user/register
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			String username = userService.saveUser(user);
			return SysResult.oK(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"新增数据失败");
	}
	
	
	@RequestMapping("/login")
	@ResponseBody
	public SysResult findUserByUP(User user){
		try {
			String ticket = userService.findUserByUP(user);
			
			if(!StringUtils.isEmpty(ticket)){
				return SysResult.oK(ticket);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户登陆失败");
	}
	
	
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public MappingJacksonValue findUserByTicket(@PathVariable String ticket,String callback){
	
		MappingJacksonValue jacksonValue = null;
		String userJSON = jedisCluster.get(ticket);
		
		if(StringUtils.isEmpty(userJSON)){
			jacksonValue = new MappingJacksonValue(SysResult.build(201,"获取用户信息失败"));
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		jacksonValue = new MappingJacksonValue(SysResult.oK(userJSON));
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
