package com.udcm.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HMAC & Signatrue 相关处理
 * @author Raul	
 * 2017年8月29日
 */
public class SecurityUtils {

	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	private static final String MAGIC_KEY = "obfuscate";
	private final static char[] HEXDICT = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

	private static ThreadLocal<MessageDigest> digest = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage(), e);
			}
			return null;
		}
	};

	/**
	 * 创建一个Token（由“用户名”：“最后登录时间”：“数字签名”三个部分组成）
	 * @param userName
	 * @param password
	 * @param lastLoginTime
	 * @return
	 */
	public static String createToken(String userName,String password,long lastLoginTime) {
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userName);
		tokenBuilder.append(":");
		tokenBuilder.append(lastLoginTime);
		tokenBuilder.append(":");
		tokenBuilder.append(computeSignature(userName,lastLoginTime,password));
		return tokenBuilder.toString();
	}


	/**
	 * 生成数字签名
	 * @param username 登录用户
	 * @param lastLoginTime 最近活跃时间
	 * @param password 用户密码
	 * @return	 
	 */
	private static String computeSignature(String username, long lastLoginTime, String password) {
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(username);
		signatureBuilder.append(":");
		signatureBuilder.append(lastLoginTime);
		signatureBuilder.append(":");
		signatureBuilder.append(password);
		signatureBuilder.append(":");
		signatureBuilder.append(SecurityUtils.MAGIC_KEY);
		byte[] byteMD5 = digest.get().digest(signatureBuilder.toString().getBytes(Charset.forName("UTF-8")));
		return byteArrayToHexString(byteMD5);
	}
	

	
	/**
	 * 签名校验
	 * @param userNameFromToken
	 * @param lastLoginTimeFromToken
	 * @param signatrueFromToken
	 * @param password
	 * @return
	 */
	public static boolean validateSignature(String userNameFromToken, long lastLoginTimeFromToken,String signatrueFromToken,String password) {
		if(null==signatrueFromToken) {
            return false;
        }
		return signatrueFromToken.equals(computeSignature(userNameFromToken, lastLoginTimeFromToken, password));
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
	public static String calculateHMAC(String data, String key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
					HMAC_SHA256_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			return Base64.encodeBase64StringUnChunked(rawHmac);
		} catch (Exception e) {
			return null;
		}
	}
		

		

		
	

}