package com.longqin.system.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.longqin.system.entity.Menu;
import com.longqin.system.entity.User;

/**
 * @Description 权限拦截器
 * @Author longqin
 * @Time: 2023年10月20日
 */
public class SecurityInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String json = (String) session.getAttribute("longqin-admin");
		User user = JSON.parseObject(json, User.class);
		if (null == user || null == user.getUserId()) {
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().print("{\"code\":403,\"msg\":\"未登录\"}");
			return false;
		}
		// 验证权限
		if (this.hasPermission(handler, user.getMenuList())) {
			return true;
		}
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().print("{\"code\":404,\"msg\":\"没有权限\"}");
		return false;
	}

	/**
	 * @Description 判断是否有权限
	 * @Author longqin
	 * @Time: 2023年10月20日
	 */
	private boolean hasPermission(Object handler, List<Menu> list) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 获取方法上的注解
			RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
			// 如果方法上的注解为空 则获取类的注解
			if (requiredPermission == null) {
				requiredPermission = handlerMethod.getMethod().getDeclaringClass()
						.getAnnotation(RequiredPermission.class);
			}
			// 如果标记了注解，则判断权限
			if (requiredPermission != null && !requiredPermission.value().isEmpty() && null != list) {
				List<String> permissionSet = new ArrayList<String>();
				for (Menu menu : list) {
					String menuUrl = menu.getMenuUrl();
					if (null != menuUrl && !"".equals(menuUrl.trim())) {
						String[] s = menuUrl.split("/");
						if (null != s && s.length > 1) {
							StringBuilder mSrt = new StringBuilder();
							for (int i = 1; i < s.length; i++) {
								mSrt.append(s[i] + ":");
							}
							mSrt.setLength(mSrt.length() - 1);// 去掉最后一个“:”
							permissionSet.add(mSrt.toString());
						}
					}
				}
				return permissionSet.contains(requiredPermission.value());
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO
	}
}
