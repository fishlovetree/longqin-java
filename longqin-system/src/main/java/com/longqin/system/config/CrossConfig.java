package com.longqin.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description 统一跨域配置
 * @Author longqin
 * @Time: 2023年10月20日
 */
@Configuration
public class CrossConfig {
	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
		corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
		corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
		corsConfiguration.setAllowCredentials(true);// 4 允许跨域携带cookie
		corsConfiguration.setMaxAge((long) 1209600);
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 5 对接口配置跨域设置
		return new CorsFilter(source);
	}
}
