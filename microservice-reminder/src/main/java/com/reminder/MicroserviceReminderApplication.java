package com.reminder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** 书签微服务
 *  6602
 * @Description 
 * @author hoob
 * @date 2019年7月11日下午4:10:01
 */
@EnableEurekaClient
@SpringBootApplication
public class MicroserviceReminderApplication {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(MicroserviceReminderApplication.class, args);
  }
}
