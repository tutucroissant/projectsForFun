package com.jt.test;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFactory {
	
	@Test
	public void testStatic(){
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/factory.xml");
		
		Calendar calendar = (Calendar) context.getBean("calendar");
		
		System.out.println("获取当前时间:"+calendar.getTime());
		
		
	}
	
	@Test
	public void testNew(){
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/factory.xml");
		Calendar calendar = (Calendar) context.getBean("calendar2");
		System.out.println("获取当前时间:"+calendar.getTime());
	}
	
	
}
