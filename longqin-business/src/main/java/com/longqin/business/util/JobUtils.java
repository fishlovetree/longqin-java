package com.longqin.business.util;

import org.springframework.stereotype.Service;

@Service
public class JobUtils{


	// 生成Cron表达式
	// 催费-常量
	// 扣费-页面定义-有不扣费
	public String getCron(int elecMethod, Integer elecDay, Integer elecHour, Integer elecMinute) {
		String cron = "";
		switch(elecMethod){
		case 1://月
			cron = "0 " + elecMinute + " " + elecHour + " " + elecDay + " * ?";
			break;
		case 2://周
			cron = "0 " + elecMinute + " " + elecHour + " ? * " + elecDay;
			break;
		case 3://日
			cron = "0 " + elecMinute + " "+ elecHour + " * * ? *";
			break;
		case 4://时
			cron = "0 " + elecMinute + " * * * ? *";
			break;
		case 5://分
			cron = "0 * * * * ? *";
			break;
		default:
			return null;
		}
		return cron;
	}
}
