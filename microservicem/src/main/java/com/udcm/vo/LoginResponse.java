/**
 * 
 */
package com.udcm.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Raul	
 * 2017年9月1日
 */
public class LoginResponse extends Response{	
	
	private String token; //平台颁发的TOKEN
	private String role;//用户角色类型
	private String userType;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	private List<String> privileges = new ArrayList<String>(); //平台赋予的权限
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<String> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
