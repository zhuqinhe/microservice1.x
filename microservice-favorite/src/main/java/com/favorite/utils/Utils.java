package com.favorite.utils;


import java.security.MessageDigest;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.favorite.constants.ConfigKey;
import com.favorite.constants.Constants;



public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);
	// 全局数组
	private final static char[] HEXDICT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	/**
	 * HMAC安全定义适合于除设备鉴权、用户注册、登录认证接口外的服务接口. 客户端需根据HMAC
	 * Data封装Json字符串，然后根据HmacSHA256加密算法使用HMAC加密Key对字符串进行加密，
	 * 将得到的加密串转换为BASE64字符串。客户端需生成HMAC，通过HTTP HEADER传递给服务端。服务端通过同样的规则生成HMAC，
	 * 与客户端传递的HMAC比较，如果不一致则为非法请求。
	 * 
	 * @param headers
	 * @param body
	 * @return
	 */
	public static int verifyHMAC(HttpServletRequest request, String body) {

		logger.debug(">>>verifyHMAC  body:" + body);

		String verifyHMAC ="";//ConfigUtil.getProperties(ConfigKey.VERIFYHMAC.name());
		if (StringUtils.isEmpty(verifyHMAC) || verifyHMAC.trim().equals("0")) {
			return Constants.SUCCESS;
		}
		else if("1".equals(verifyHMAC.trim())){
			try {
				String token = request.getHeader("userToken");
				String terminalType = request.getHeader("terminalType");
				String deviceId = request.getHeader("deviceId");
				String timestamp = request.getHeader("timestamp");
				String timezone = request.getHeader("timezone");
				String random = request.getHeader("random");
				String hmac = request.getHeader("hmac");
				// 无认证字符串
				if (hmac == null || hmac.trim().equals("")) {
					return Constants.NO_VERIFY_STRING;
				}
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
				if (body != null && !body.equals("")) {
					strb.append(body).append("}");
				} else {
					strb.append("{}}");
				}
				// 产生key
				String key = createMD5(createMD5(token) + token.substring(5, 10));
				// 服务端产生hmac
				String hmacServer = calculateHMAC(strb.toString(), key);
				// 服务端和客户端的HMAC相等，则验证通过
				if (hmacServer != null && hmacServer.equals(hmac)) {
					return Constants.SUCCESS;
				}	
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return Constants.VERIFY_FAILURE;
			}
		}else if("2".equals(verifyHMAC.trim())){
			try {
				String token = request.getHeader("Token");
				String terminalType = request.getHeader("Terminal-Type");
				String deviceId = request.getHeader("Device-Id");
				String timestamp = request.getHeader("Timestamp");
				String timezone =request.getHeader("Timezone-Offset");
				String random =request.getHeader("Random"); 
				String serviceId = request.getHeader("Service-Id"); 
				String hmac =request.getHeader("Hmac");
				// 无认证字符串
				if (hmac == null || hmac.trim().equals("")) {
					return Constants.NO_VERIFY_STRING;
				}
				// 拼装Json字符串
				StringBuilder strb = new StringBuilder();
				strb.append("{\"Header\":{\"Token\":\"").append(token).append("\",").append("\"Terminal-Type\":\"")
				.append(terminalType).append("\",").append("\"Device-Id\":\"").append(deviceId).append("\",")
				.append("\"TS1\":\"").append(timestamp).append("\",").append("\"TSZ\":\"").append(timezone)
				.append("\",").append("\"RND\":\"").append(random).append("\",").append("\"Service-Id\":\"")
				.append(serviceId).append("\"},\"Body\":");
				if (body != null && !body.equals("")) {
					strb.append(body).append("}");
				} else {
					strb.append("{}}");
				}
				// 产生key
				String key = createMD5(createMD5(token) + token.substring(5, 10));
				// 服务端产生hmac
				String hmacServer = calculateHMACNew(strb.toString(), key);
				// 服务端和客户端的HMAC相等，则验证通过
				if (hmacServer != null && hmacServer.equals(hmac)) {
					return Constants.SUCCESS;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return Constants.VERIFY_FAILURE;
			}

		}
		return Constants.VERIFY_FAILURE;
	}
	
	/**
	 * 产生MD5码
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String createMD5(String src) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(src.getBytes("UTF-8"));
		return byteArrayToHexString(md5.digest());
	}
	
	/**
	 * 字节数组转十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteArrayToHexString(byte[] byteArray) {
		int len = byteArray.length;
		StringBuilder result = new StringBuilder(len << 1);
		for (int i = 0; i < len; i++) {
			byte b = byteArray[i];
			result.append(HEXDICT[(b >>> 4) & 0x0F]);
			result.append(HEXDICT[b & 0x0F]);
		}
		return result.toString();
	}

	/**
	 * 根据HmacSHA256加密算法使用HMAC加密Key对字符串进行加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static String calculateHMACNew(String data, String key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
            return 	Base64.encodeBase64StringUnChunked(rawHmac);
		} catch (Exception e) {
			return null;
		}
	}
	public static String calculateHMAC(String data, String key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),"HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			return org.jboss.resteasy.util.Base64.encodeBytes(rawHmac);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	public static String arrayToString(String[] strArr) {

		if (strArr == null || strArr.length == 0) {
			return "";
		}
		Set<String> set = new TreeSet<>();
		for (String str : strArr) {
			set.add(str);
		}
		StringBuffer sb = new StringBuffer("");
		for (String str : set) {
			sb.append(str).append(",");
		}
		return sb.toString();
	}
	
	public static boolean objIsNull(Object... args) {
		for (Object obj : args) {
			if (obj == null || obj.toString().trim().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static String obj2Str(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	public static int obj2Int(Object obj) {
		String str = obj2Str(obj);
		if (str.matches("\\d+")) {
			return Integer.parseInt(str);
		}
		return 0;
	}

	public static int obj2Int(Object o, int defaultValue) {

		String str = obj2Str(o);
		if (str.matches("\\d+")) {
			return Integer.parseInt(str);
		}
		return defaultValue;
	}
}
