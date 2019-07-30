package com.fonsview.metadata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fonsview.metadata.utils.StringUtils;
@Component
@ConfigurationProperties(prefix = "metadata.config")
public class Config {
	//#默认语言配置
	private String default_language;
	//#图片拼接地址 310cms中的图片地址是相对地址，
	private String picture_url_prefix;
	
	//默认使用的cdn服务器地址
	private String defaultCDN;
	
	//但盗链加密串
	private String passportkey;
	//防盗链加密串版本号
	private String  version;
	
	
	public String getPassportkey() {
		return passportkey;
	}
	public String getVersion() {
		if(StringUtils.isEmpty(version)){
			version="1";
		}
		return version;
	}
	public void setPassportkey(String passportkey) {
		this.passportkey = passportkey;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDefaultCDN() {
		return defaultCDN;
	}
	public void setDefaultCDN(String defaultCDN) {
		this.defaultCDN = defaultCDN;
	}
	public String getDefault_language() {
		return default_language;
	}
	public String getPicture_url_prefix() {
		return picture_url_prefix;
	}
	public void setDefault_language(String default_language) {
		this.default_language = default_language;
	}
	public void setPicture_url_prefix(String picture_url_prefix) {
		this.picture_url_prefix = picture_url_prefix;
	}
	
}