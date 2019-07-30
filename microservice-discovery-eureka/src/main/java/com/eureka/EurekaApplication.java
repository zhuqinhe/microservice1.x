package com.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * eureka 注册server 后续需要考虑高可用
 * 暂不开启安全认证   
 * 端口规划  默认8761
 * @Description 
 * @author hoob
 * @date 2019年7月11日上午11:49:27
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
  }
}
