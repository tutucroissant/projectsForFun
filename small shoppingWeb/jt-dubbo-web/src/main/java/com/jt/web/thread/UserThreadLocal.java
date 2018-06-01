package com.jt.web.thread;

import java.util.Map;

import com.jt.web.pojo.User;

public class UserThreadLocal {
	
	private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
	
	
	public static User get(){
		
		return userThreadLocal.get();
	}
	
	public static void set(User user){
		
		userThreadLocal.set(user);
	}
	
	//切记将ThreadLocal数据清空,为了防止 内存溢出(泄漏)
	public static void remove(){
		userThreadLocal.remove();
	}
	
}
