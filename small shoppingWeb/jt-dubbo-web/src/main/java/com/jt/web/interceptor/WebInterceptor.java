package com.jt.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.web.pojo.User;
import com.jt.web.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class WebInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	//需求:要求用户在没有登陆的前提下 跳转到登陆页面
	/**
	 * 如何实现需求:
	 * 1.获取用户的Cookie信息
	 * 	 判断cookie中是否有数据
	 * 2.获取缓存数据 
	 *  2.1如果有缓存数据 则请求放行 return true
	 *  2.2如果缓存中没有数据  则跳转到登陆页面
	 */
	
	//1.在请求方法执行之前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取ticket值
		Cookie[] cookies = request.getCookies();
		
		String ticket = null;
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())){
				
				ticket = cookie.getValue();
			}
		}
		//判断是否含有ticket值
		if(!StringUtils.isEmpty(ticket)){
			
			String userJSON = jedisCluster.get(ticket);
			//判断是否还有数据
			if(!StringUtils.isEmpty(userJSON)){
				
				User user = objectMapper.readValue(userJSON, User.class);
				UserThreadLocal.set(user);
				//System.out.println("用户信息已经保存");
				return true; //表示用户已经登陆
			}
		}
		//转向到登陆页面
		response.sendRedirect("/user/login.html");
		return false;
	}
	
	//请求处理完成之后   视图解析器调用之前
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//响应结束时
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//将本地线程变量清空
		UserThreadLocal.remove();
		//System.out.println("删除本地线程");
		
	}

}
