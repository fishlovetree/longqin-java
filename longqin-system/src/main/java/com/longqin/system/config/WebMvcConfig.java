package com.longqin.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 权限拦截器配置
 * @Author longqin
 * @Time: 2023年10月20日
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public SecurityInterceptor securityInterceptor() {
		return new SecurityInterceptor();
	}

	/**
	 * @Description 添加拦截器
	 * @Author longqin
	 * @Time: 2023年10月20日
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 添加登录处理拦截器，拦截所有请求，登录请求除外
		InterceptorRegistration interceptorRegistration = registry.addInterceptor(securityInterceptor());
		// 排除配置
		interceptorRegistration.excludePathPatterns("/login");
		interceptorRegistration.excludePathPatterns("/logout");
		interceptorRegistration.excludePathPatterns("/sendCode");
		interceptorRegistration.excludePathPatterns("/register");
		interceptorRegistration.excludePathPatterns("/notlogon");
		interceptorRegistration.excludePathPatterns("/unauth");
		interceptorRegistration.excludePathPatterns("/swagger-ui.html/**");
		interceptorRegistration.excludePathPatterns("/webjars/**");
		interceptorRegistration.excludePathPatterns("/swagger-resources/**");
		interceptorRegistration.excludePathPatterns("/v2/**");
		interceptorRegistration.excludePathPatterns("/**/error");
		//interceptorRegistration.excludePathPatterns("/uploads/**");
		// 配置拦截策略
		interceptorRegistration.addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 解决 swagger-ui.html 404报错
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		
		String os = System.getProperty("os.name").toLowerCase();
		String path = null;
		if (os.contains("windows")) {
			path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\uploads\\";
		}else if (os.contains("linux")) {
			path = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
		}
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + path);
	}
}