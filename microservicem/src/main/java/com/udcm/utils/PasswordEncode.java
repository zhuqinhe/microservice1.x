package com.udcm.utils;

import java.security.MessageDigest;

public class PasswordEncode {

	public String encode(CharSequence rawPassword) {
		return md5Password(rawPassword.toString());
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword.toString().equalsIgnoreCase(encodedPassword)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String encodePassword(String password) {
		return md5Password(password.toString());
	}
	
	public static String md5Password(String password) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = password.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
