package com.reminder.utils;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.apache.commons.net.util.Base64;

import com.reminder.constants.TerminalType;


/**
 * 台湾中嘉 HMAC 加密算法。
 * @author Administrator
 *
 */
public class Header {

	private static final String TOKEN = "userToken";
	private static final String TERMINAL_TYPE = "terminalType";
	private static final String SERVICE_ID = "Service-Id";
	private static final String DEVICE_ID = "deviceId";
	private static final String TIMEZONE_OFFSET = "timezone";
	private static final String TIMESTAMP = "timestamp";
	private static final String RANDOM = "random";
	private static final String HMAC = "hmac";

	private final static Logger log = Logger.getLogger(Header.class);

	// 设置header 请求头
	public static Map<String, String> headers(String token, String deviceId, TerminalType terminalType, String body)
			throws Exception {
		Map<String, String> headers = new HashMap<String, String>();

		// 时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());

		// 生成随机值
		UUID uuid = UUID.randomUUID();
		String randoms = uuid.toString();

		// 获取时区
		Calendar cal = Calendar.getInstance();
		TimeZone timezone = cal.getTimeZone();
		String timezoneOffset = timezone.getID();

		// 终端类型
		if (null == terminalType) {
			terminalType = TerminalType.NULL;
		}

		// Token
		if (null == token) {
			token = randoms;
		}

		// Identify
		if (null == deviceId) {
			deviceId = "";
		}

		// Service ID
		String serviceId = "";

		// Body
		if (null == body) {
			body = "";
		}

		headers.put(TOKEN, token);
		headers.put(TERMINAL_TYPE, String.valueOf(terminalType.getValue()));
		headers.put(SERVICE_ID, serviceId);
		headers.put(DEVICE_ID, deviceId);
		headers.put(TIMEZONE_OFFSET, timezoneOffset);
		headers.put(TIMESTAMP, timestamp);
		headers.put(RANDOM, randoms);

		// 拼装Json字符串
		StringBuilder strb = new StringBuilder();
		strb.append("{\"Header\":{\"token\":\"").append(token).append("\",").append("\"terminalType\":\"")
				.append(terminalType.getValue()).append("\",").append("\"deviceId\":\"").append(deviceId).append("\",")
				.append("\"TS1\":\"").append(timestamp).append("\",").append("\"TSZ\":\"").append(timezoneOffset)
				.append("\",").append("\"RND\":\"").append(randoms).append("\"},\"Body\":");
		if (body != null && !body.equals("")) {
			strb.append(body).append("}");
		} else {
			strb.append("{}}");
		}

		log.info(">>>>> header=" + strb);
		// KEY 文件头加密 生成HMAC
		String Key = createMD5(createMD5(token) + token.substring(5, 10));
		// 服务端产生hmac
		String hmac = calculateHMAC(strb.toString(), Key);

		System.out.println("HMAC:" + hmac);
		log.info("HMAC:" + hmac);

		// 加密处理
		headers.put(HMAC, hmac);
		return headers;
	}

	// 全局数组
	private final static char[] HEXDICT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

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
	public static String calculateHMAC(String data, String key) {
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

}
