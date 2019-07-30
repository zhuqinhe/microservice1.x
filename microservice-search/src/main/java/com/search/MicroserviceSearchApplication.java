package com.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** 书签微服务
 *  6602
 * @Description 
 * @author hoob
 * @date 2019年7月11日下午4:10:01
 */

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
public class MicroserviceSearchApplication {

  public static void main(String[] args) {
    SpringApplication.run(MicroserviceSearchApplication.class, args);
  }
}
