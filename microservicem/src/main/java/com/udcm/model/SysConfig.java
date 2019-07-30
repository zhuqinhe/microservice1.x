package com.udcm.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 系统配置实体类
 * 
 * @author makefu 2016年4月14日
 *
 */

@Document(collection = "sys_config")
public class SysConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Indexed(name="key", unique=true, dropDups=false)
	private String key; // 配置KEY

	private String value; // 配置值

	private Boolean enable = true; // 是否启用

	private String desc; // 描述

	private String name; // 名称
	
	private Date createTime;
	
	private Date updateTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}



	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String toString() {
		return "SysConfig [key=" + key + ", value=" + value + ", enable=" + enable + ", desc=" + desc + ", name="
				+ name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
