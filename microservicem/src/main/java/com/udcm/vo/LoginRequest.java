/**
 * 
 */
package com.udcm.vo;


/**
 * @author Raul	
 * 2017年9月1日
 */
public class LoginRequest extends Request{
	
	private String requestId; //登录请求会话ID 与  获取验证码保持一致
	
	private String userId; //用户ID
	
	private String password; //MD5加密后的密码
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	private String captcha; //验证码	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

}
