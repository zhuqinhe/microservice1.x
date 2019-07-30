/**
 * 
 */
package com.zuul.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zuul.config.Config;

/**
 * @Description 
 * @author hoob
 * @date 2019年7月18日下午7:59:40
 */
public class HmacUtil {
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	/**
	 * HMAC校验（防篡改）
	 * @param request
	 * @param requestBody
	 * @return 
	 * @return
	 */
	public static boolean checkHMAC(HttpServletRequest request,String requestBody,Config config){
	
		if (StringUtils.isEmpty(config.getVerifyHMAC()) || "0".equals(config.getVerifyHMAC())) return true;
		if(null==request) return false;
		if(null==requestBody) requestBody = "";
		String token = request.getHeader("Token");
		if(StringUtils.isNotEmpty(token)){
			String clientHmac = request.getHeader("Hmac");
			String terminalType = request.getHeader("Terminal-Type");
			String deviceId = request.getHeader("Device-Id");
			String timestamp = request.getHeader("Timestamp");
			String timezone = request.getHeader("Timezone-Offset");
			String random = request.getHeader("Random");
			String serviceId = request.getHeader("Service-Id");		
			StringBuilder strb = new StringBuilder();
			strb.append("{\"Header\":{\"Token\":\"").append(token).append("\",")
					.append("\"Terminal-Type\":\"").append(terminalType)
					.append("\",").append("\"Device-Id\":\"").append(deviceId)
					.append("\",").append("\"TS1\":\"").append(timestamp)
					.append("\",").append("\"TSZ\":\"").append(timezone)
					.append("\",").append("\"RND\":\"").append(random)
					.append("\",").append("\"Service-Id\":\"").append(serviceId).append("\"}");
			strb.append(",");
			if(null==requestBody || requestBody.trim().isEmpty()){
				strb.append("\"Body\":").append("{}");
			}else{
				strb.append("\"Body\":").append(requestBody).append("");
			}				
			strb.append("}");				
			//KEY 文件头加密 生成HMAC
			String Key = null;
			try {
				Key = SecurityUtil.createMD5(SecurityUtil.createMD5(token) + token.substring(5, 10));
			} catch (Exception e) {
				log.error(e.getMessage(),e);		
			}
			//服务端产生hmac
			String hmac = SecurityUtil.calculateHMAC(strb.toString(), Key);
			return clientHmac.equals(hmac);
		}else{
			String clientHmac = request.getHeader("hmac");
			 token = request.getHeader("userToken");
			String terminalType = request.getHeader("terminalType");
			String deviceId = request.getHeader("deviceId");
			String timestamp = request.getHeader("timestamp");
			String timezone = request.getHeader("timezone");
			String random = request.getHeader("random");
			// 拼装Json字符串
			StringBuilder strb = new StringBuilder();
			strb.append("{\"Header\":{\"token\":\"").append(token)
			.append("\",").append("\"terminalType\":\"")
			.append(terminalType).append("\",")
			.append("\"deviceId\":\"").append(deviceId).append("\",")
			.append("\"TS1\":\"").append(timestamp).append("\",")
			.append("\"TSZ\":\"").append(timezone).append("\",")
			.append("\"RND\":\"").append(random)
			.append("\"},\"Body\":");
			if (requestBody != null && !requestBody.equals("")) {
				strb.append(requestBody).append("}");
			} else {
				strb.append("{}}");
			}
			//KEY 文件头加密 生成HMAC
			String Key = null;
			try {
				Key = SecurityUtil.createMD5(SecurityUtil.createMD5(token) + token.substring(5, 10));
			} catch (Exception e) {
				log.error(e.getMessage(),e);		
			}
			//服务端产生hmac
			String hmac = SecurityUtil.calculateHMAC(strb.toString(), Key);
			return clientHmac.equals(hmac);
		}
	}
}
