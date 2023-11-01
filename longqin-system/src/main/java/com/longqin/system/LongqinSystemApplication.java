package com.longqin.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan(value = { "com.longqin.system.mapper" })
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=7200)
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
public class LongqinSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LongqinSystemApplication.class, args);
	}

}
