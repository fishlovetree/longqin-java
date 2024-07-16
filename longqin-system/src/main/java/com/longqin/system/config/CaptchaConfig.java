package com.longqin.system.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration  
public class CaptchaConfig {  
  
    @Bean  
    public DefaultKaptcha producer() {  
        Properties properties = new Properties();  
        properties.put("kaptcha.border", "yes");  
        properties.put("kaptcha.border.color", "105,179,90");  
        properties.put("kaptcha.textproducer.font.color", "blue");  
        properties.put("kaptcha.image.width", "250");  
        properties.put("kaptcha.image.height", "90");  
        properties.put("kaptcha.textproducer.font.size", "40");  
        properties.put("kaptcha.session.key", "captchaText");  
        properties.put("kaptcha.textproducer.char.space", "5");  
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");  
  
        // 创建验证码配置实例
        Config config = new Config(properties); 
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;  
    }  
}
