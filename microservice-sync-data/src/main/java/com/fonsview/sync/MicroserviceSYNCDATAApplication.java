package com.fonsview.sync;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * @author Administrator
 * 点评微服务
 * 端口规划 6601
 */
//@Configuration
//@EnableAutoConfiguration //自动加载配置信息
//@EnableCircuitBreaker 
@SpringBootApplication
@EnableEurekaClient
public class MicroserviceSYNCDATAApplication {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(MicroserviceSYNCDATAApplication.class, args);
  }
}
