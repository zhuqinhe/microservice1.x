package com.zuul.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "customize.config")
public class Config {

	//自定义配置
	private String verifyHMAC;

	public String getVerifyHMAC() {
		return verifyHMAC;
	}

	public void setVerifyHMAC(String verifyHMAC) {
		this.verifyHMAC = verifyHMAC;
	}

	
}