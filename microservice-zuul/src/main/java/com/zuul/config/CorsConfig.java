package com.zuul.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description spring boot  允许跨域设置
 * @author hoob
 * @date 2018年11月17日下午6:28:47
 */
@Configuration 
public class CorsConfig {
    //限制请求头
	
    @Bean
    public FilterRegistrationBean corsFilter() {

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 1
        config.addAllowedHeader("Origin, X-Requested-With, Content-Type, Accept, deviceid, hmac, random, terminaltype, timestamp, timezone, usertoken"); // 2
        config.addAllowedMethod("GET, POST, DELETE, PUT"); // 3
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;

    }
}