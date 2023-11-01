package com.longqin.system.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.longqin.system.entity.User;

public class SessionUtil {
	
	private static final String SESSION_NAME = "longqin-admin";
	
	public static String getSessionId(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request.getSession().getId();
	}
	
	public static void setSessionUser(User user){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_NAME, JSON.toJSONString(user));
	}

	public static User getSessionUser(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();
		String json = (String) session.getAttribute(SESSION_NAME);
		return JSON.parseObject(json, User.class);
	}
	
	public static void removeSessionUser(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_NAME);
		session.invalidate();
	}
}
