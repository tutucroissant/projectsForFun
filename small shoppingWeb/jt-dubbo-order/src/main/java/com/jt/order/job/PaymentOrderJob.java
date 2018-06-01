package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;

public class PaymentOrderJob extends QuartzJobBean{
	
	/**
	 * 1.首先获取Spring容器
	 * 2.获取orderMapper对象
	 * 3.编辑超时后的业务处理Sql
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ApplicationContext springContext = 
		(ApplicationContext) 
		context.getJobDetail().getJobDataMap().get("applicationContext");
		
		OrderMapper orderMapper = springContext.getBean(OrderMapper.class);
		
		//生成2天之前的时间
		Date agoDate = new DateTime().minusDays(2).toDate();
		
		//实现订单超时处理     
		orderMapper.updateStatusByDate(agoDate);
		System.out.println("订单任务执行完成");
	}
}
