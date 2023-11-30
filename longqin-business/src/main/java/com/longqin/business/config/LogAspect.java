package com.longqin.business.config;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.longqin.business.entity.Log;
import com.longqin.business.entity.User;
import com.longqin.business.feign.FeignService;
import com.longqin.business.util.OperationLog;
import com.longqin.business.util.SessionUtil;

/**
 * @Description 统一日志处理
 * @Author longqin
 * @Time: 2023年10月20日
 */
@Aspect
//交给spring管理
@Component
//该切面的优先顺序，越小优先级越高
@Order(value = 1)
public class LogAspect {
	
	@Resource
	private FeignService feignService;
	
	@Pointcut("@annotation(com.longqin.business.util.OperationLog)")
	public void operationLog() {

	}

	@AfterReturning("operationLog()")
	public void doAfterReturning(JoinPoint jp) {
		Method proxyMethod = ((MethodSignature) jp.getSignature()).getMethod();
		OperationLog operationLog = proxyMethod.getAnnotation(OperationLog.class);
		Object[] args = jp.getArgs();
		StringBuilder sBuilder = new StringBuilder();
		DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
		String[] parameterNames = parameterNameDiscoverer.getParameterNames(proxyMethod);
		if (parameterNames != null && parameterNames.length > 0) {
			EvaluationContext context = new StandardEvaluationContext();
			for (int i = 0; i < args.length; i++) {
				// 替换spel里的变量值为实际值，比如 #user -->user对象
				context.setVariable(parameterNames[i], args[i]);
			}
			// 解析出实际的日志信息
			SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
			String opeationInfo = spelExpressionParser.parseExpression(operationLog.content()).getValue(context).toString();
			sBuilder.append(opeationInfo);
		}
		addLog(operationLog.title(), sBuilder.toString(), Integer.parseInt(operationLog.operationType()));
	}
	
	public void doAfterThrow(JoinPoint jp, Throwable ex) {
		
	}
	
	/**
	 * @Description 存储日志信息
	 * @Author longqin
	 * @Time 2023年10月20日
	 * @param type-操作类型：增-0;删-1;改-2；启用-3；停用-4；请求-5；响应-6；设置-7
	 */
    public void addLog(String title, String content, Integer type) {
		try {
			// 创建日志对象
			Log mLog = new Log();
			// 从session获取用户
			User user = SessionUtil.getSessionUser();
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			// 获取IP
			String ip = getIpAddr(request);

			mLog.setCreator(user.getUserId());
			mLog.setOrganizationId(user.getOrganizationId());
			mLog.setTitle(title);
			mLog.setRemark(content);
			mLog.setIp(ip);
			mLog.setOperateType(type);
			
			// 将日志存入数据库
			feignService.addLog(mLog);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
	 * @Description 获取当前网络ip(可以获取通过了Apache，Squid等反向代理软件的客户端的真实IP地址)
	 * @Author longqin
	 * @Time 2023年10月20日
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * @Description 获取来访者的浏览器版本
	 * @Author longqin
	 * @Time 2023年10月20日
	 */
	public String getRequestBrowserInfo(HttpServletRequest request) {
		String browserVersion = null;
		String agent = request.getHeader("User-Agent").toLowerCase();
		if (agent == null || agent.equals("")) {
			return "";
		}
		// System.out.print(agent);
		if (agent.indexOf("msie 7") > 0) {
			browserVersion = "IE7";
		} else if (agent.indexOf("msie 8") > 0) {
			browserVersion = "IE8";
		} else if (agent.indexOf("msie 9") > 0) {
			browserVersion = "IE9";
		} else if (agent.indexOf("msie 10") > 0) {
			browserVersion = "IE10";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			browserVersion = "IE11";
		} else if (agent.indexOf("opera") > 0) {
			browserVersion = "Opera";
		} else if (agent.indexOf("firefox") > 0) {
			browserVersion = "Firefox";
		} else if (agent.indexOf("chrome") > 0) {
			browserVersion = "Chrome";
		} else if (agent.indexOf("webkit") > 0) {
			browserVersion = "Webkit";
		} else if (agent.indexOf("safari") > 0) {
			browserVersion = "Safari";
		} else if (agent.indexOf("camino") > 0) {
			browserVersion = "Camino";
		} else if (agent.indexOf("konqueror") > 0) {
			browserVersion = "Konqueror";
		} else {
			browserVersion = "Others";
		}
		return browserVersion;
	}

}
