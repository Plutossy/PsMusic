package com.example.psmusic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
* 解决跨域问题
* */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域的路径
                .allowedOriginPatterns("*") // 允许跨域的域名，可以用*表示允许任何域名使用
                .allowedMethods("*") // 允许任何方法（post、get等）
                .allowCredentials(true); //允许携带cookie
    }
}
