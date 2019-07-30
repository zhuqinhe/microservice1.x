package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 微服务测试接入入口  
 * 端口规划  8080
 * @Description 
 * @author hoob
 * @date 2019年7月11日下午1:48:31
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker 
public class MicroserviceTestCaseApplication {
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
	    return restTemplate;
	}
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceTestCaseApplication.class, args);
	}
}
