package com.udcm.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户登录成功后的会话信息
 * @author Raul	
 * 2017年8月29日
 */
public class UserSession {

	private String userId;//用户ID

	private String password;//用户密码
	
	private Date lastActiveTime;//最近活跃时间
	
	private List<String> ifPrivileges = new ArrayList<String>();//接口授权	
	private List<String> menuPrivileges = new ArrayList<String>();//菜单授权	

	public List<String> getMenuPrivileges() {
		return menuPrivileges;
	}

	public void setMenuPrivileges(List<String> menuPrivileges) {
		this.menuPrivileges = menuPrivileges;
	}

	public List<String> getIfPrivileges() {
		return ifPrivileges;
	}

	public void setIfPrivileges(List<String> ifPrivileges) {
		this.ifPrivileges = ifPrivileges;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
