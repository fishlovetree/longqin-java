package com.longqin.business.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.longqin.business.entity.Errorlog;
import com.longqin.business.entity.User;
import com.longqin.business.feign.FeignService;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.longqin.business.util.SessionUtil;

/**
 * @Description 统一异常处理
 * @Author longqin
 * @Time: 2023年10月20日
 */
@RestControllerAdvice
public class ExceptionHandlers {
	Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);
	
	@Resource
	FeignService feignService;

	@ExceptionHandler(value = Exception.class)
	public ResponseData handle(Exception e) {
		logger.info(e.toString());
		saveException(e); // 将异常写入数据库
		return new ResponseData(ResponseEnum.ERROR.getCode(), e.getMessage(), null);
	}

	/**
	 * @Description 存储异常信息
	 * @Author longqin
	 * @Time 2023年10月20日
	 */
	public void saveException(Exception ex) {
		try {
			// 创建日志对象
			Errorlog merrorlog = new Errorlog();
			// 获取系统时间
			@SuppressWarnings("unused")
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			// 从session获取用户
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			User user = SessionUtil.getSessionUser();
			
			// 获取IP
			String ip = getIpAddr(request);
			if (null != user) {
				merrorlog.setUserId(user.getUserId().intValue());
				merrorlog.setIp(ip);
				String Agent = getRequestBrowserInfo(request);
				merrorlog.setBroswer(Agent);
				if (null != ex.getMessage() && ex.getMessage().length() > 1800) {
					merrorlog.setMessage(ex.getMessage().substring(0, 1800));// 异常消息
				} else {
					merrorlog.setMessage(ex.getMessage());// 异常消息
				}
				merrorlog.setStacktrace(getStackTrace(ex));// 异常堆栈
				merrorlog.setErrorClass(ex.getClass().toString());// 异常类
				// 将日志存入数据库
				feignService.addException(merrorlog);
			}

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

	public String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			throwable.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
}
