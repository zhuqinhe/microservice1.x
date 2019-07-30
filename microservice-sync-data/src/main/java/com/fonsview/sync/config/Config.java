package com.fonsview.sync.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fonsview.sync.utils.StringUtils;

@Component
@ConfigurationProperties(prefix = "sync.config")
public class Config {

	//#cms版本号区分是cms300还是是cms310   对应的配置值分别是   300,310
	private String cmsversion;
	//#默认语言配置
	private String default_language;
	//#图片拼接地址 310cms中的图片地址是相对地址，
	private String picture_url_prefix;
	//#图片替换地址，原cms300的是绝对地址，需要替换的
	private String picture_url_prefix_old;
	private String picture_url_prefix_new;
	public String getCmsversion() {
		if(StringUtils.isEmpty(cmsversion)){
			cmsversion="300";
		}
		return cmsversion;
	}
	public String getDefault_language() {
		if(StringUtils.isEmpty(default_language)){
			default_language="zh_CN";
		}
		return default_language;
	}
	public String getPicture_url_prefix() {
		return picture_url_prefix;
	}
	public String getPicture_url_prefix_old() {
		return picture_url_prefix_old;
	}
	public String getPicture_url_prefix_new() {
		return picture_url_prefix_new;
	}
	public void setCmsversion(String cmsversion) {
		this.cmsversion = cmsversion;
	}
	public void setDefault_language(String default_language) {
		this.default_language = default_language;
	}
	public void setPicture_url_prefix(String picture_url_prefix) {
		this.picture_url_prefix = picture_url_prefix;
	}
	public void setPicture_url_prefix_old(String picture_url_prefix_old) {
		this.picture_url_prefix_old = picture_url_prefix_old;
	}
	public void setPicture_url_prefix_new(String picture_url_prefix_new) {
		this.picture_url_prefix_new = picture_url_prefix_new;
	}

}