package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	@RequestMapping("/{module}")
	public String toModule(@PathVariable String module){
		
		return module;
	}
	
	//实现用户的注册功能  /service/user/doRegister
	@RequestMapping("/doRegister")  
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			String username = userService.saveUser(user);
			if(!StringUtils.isEmpty(username)){
				
				return SysResult.oK();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"用户注册失败");
	}
	
	
	/**
	 * -1 表示当前会话结束 cookie删除了
	 *  0 直接删除cookie
	 *  >0的数值 表示存活多少秒 
	 *  
	 * cookie.setPath("/"); 表示cookie共享使用的
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response){
		try {
			//1.获取ticket信息
			String ticket = userService.findUserByUP(user);
			
			if(!StringUtils.isEmpty(ticket)){
				//2.将ticket信息写入cookie中
				Cookie cookie = new Cookie("JT_TICKET", ticket);
				cookie.setMaxAge(60 * 60 * 48);
				cookie.setPath("/");
				//cookie.setDomain();  根据当前域名自动添加
				response.addCookie(cookie);
				return SysResult.oK(); //正确返回
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"登录失败");
	}
	
	/**
	 * 用户登出操作
	 * 1.如何获取cookie中的值
	 * 2.获取ticket值删除缓存
	 * 3.将Cookie删除
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		
		String key = "JT_TICKET";
		String ticket = null;
		Cookie[] cokies = request.getCookies();
		
		for (Cookie cookie : cokies) {
			//判断是否为jt-ticket之后获取属性值
			if(key.equals(cookie.getName())){
				ticket = cookie.getValue();
				System.out.println("获取ticket信息:"+ticket);
			}
		}
		//1.清空缓存数据
		jedisCluster.del(ticket);
		
		//2.删除Cookie
		Cookie cookie = new Cookie(key, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		//return "forward:/转发地址";
		return "redirect:/index.html"; //转发操作 当前情况使用重定向操作
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
