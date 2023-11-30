package com.longqin.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan(value = { "com.longqin.business.mapper" })
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=7200)
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
@EnableEurekaClient
@EnableFeignClients
public class LongqinBusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(LongqinBusinessApplication.class, args);
	}

}
