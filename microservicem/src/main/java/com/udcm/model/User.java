/**
 * 
 */
package com.udcm.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.udcm.utils.CustomDateDeserializer;
/**
 * @Description 
 * @author hoob
 * @date 2019年5月24日上午10:50:25
 */
@Document(collection = "user")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Type {
		/*
		* 超级管理员，管理员，普通用户
		* */
		SYSADMIN, ADMIN, USER
	}
    private String id;
	
	private String password;

	private String nickName;// 昵称

	private String organization;// 所属机构
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date lastLoginTime;// 最近登录时间

	private Integer loginTimes = 0;// 登录次数

	private Boolean enable = true;// 是否启用

	private Boolean enableIPBinding = false; // 是否启用IP绑定

	private String ipAddr;
	
	private String parentUserId; // 父级ID

	private Integer userlevel = 1; // 层级 关系

	private String userType; // 超级管理员 管理员 用户 sysadmin admin user

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date pwdUpdateTime;// 最近密码更新时间
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;// 最近密码更新时间
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreateTime() {
		return createTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Indexed(name="userId", unique=true, dropDups=false)
	private String userId; // 用户登录名

	public String getPassword() {
		return password;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public String getOrganization() {
		return organization;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public Boolean getEnable() {
		return enable;
	}

	public Boolean getEnableIPBinding() {
		return enableIPBinding;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public String getUserId() {
		return userId;
	}

	public String getParentUserId() {
		return parentUserId;
	}

	public Integer getUserlevel() {
		return userlevel;
	}

	public String getUserType() {
		return userType;
	}

	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public void setEnableIPBinding(Boolean enableIPBinding) {
		this.enableIPBinding = enableIPBinding;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	public void setUserlevel(Integer userlevel) {
		this.userlevel = userlevel;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}


}
