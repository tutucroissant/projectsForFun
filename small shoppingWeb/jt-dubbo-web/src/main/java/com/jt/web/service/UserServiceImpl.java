package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private HttpClientService httpClient;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	
	@Override
	public String saveUser(User user) {
		String uri = "http://sso.jt.com/user/register";
		
		//封装请求的参数
		Map<String,String> params = new HashMap<String,String>();
		params.put("username",user.getUsername());
		params.put("password",user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		String jsonData = httpClient.doPost(uri, params);
		try {
			SysResult sysResult =
					 objectMapper.readValue(jsonData,SysResult.class);
			if(sysResult.getStatus() == 200){
				
				return (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//用户登陆
	@Override
	public String findUserByUP(User user) {
		String uri = "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		String jsonData = httpClient.doPost(uri, params);
		//将数据转化为对象
		try {
			SysResult sysResult = 
			objectMapper.readValue(jsonData, SysResult.class);
			if(sysResult.getStatus() == 200){	
				return (String) sysResult.getData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
