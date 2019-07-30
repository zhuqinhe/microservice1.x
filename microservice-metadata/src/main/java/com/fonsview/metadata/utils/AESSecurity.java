package com.fonsview.metadata.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.DerivedKey;
import org.apache.commons.ssl.OpenSSL;
import org.apache.log4j.Logger;

/**
 * CDN测试AES加密算法 加密串(AuthInfo)＝AES（USERID＋”$$” ＋IP ＋”$$” ＋TimeStamp＋”$$” ＋Flag，
 * 密钥(Key)）
 * 
 * @author hehx
 * 
 */
public final class AESSecurity {

	private static final int ENCRYPT_KEY_SIZE = 16;

	private static final int DEFAULT_BUFF_SIZE = 128;

	/**
	 * 空的字节数组。
	 */
	private static final byte[] EMPTY_BYTES = new byte[0];

	public static byte[] iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0 };

	public static byte[] key1 = new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65,
			0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f, 0x70 };
	static Logger log = Logger.getLogger(AESSecurity.class);

	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	/**
	 * AES加密算(CDN测试)
	 * 
	 * @param subUserId
	 *            用户账号
	 * @param ip
	 *            客户端IP
	 * @param physicContentId
	 *            physicContentId
	 * @param second
	 *            以秒为单位的时间戳,当前时间
	 * @param flag
	 *            标志位 1
	 * @param passwd
	 *            密钥
	 * @return 返回加密后字符串。
	 */
	public static String encryptECB(String subUserId, String ip,
			String physicContentId, long second, int flag, String passwd) {
		final String msg = new StringBuilder(DEFAULT_BUFF_SIZE)
				.append(subUserId).append("$$").append(ip).append("$$")
				.append(physicContentId).append("$$").append(second)
				.append("$$").append(flag).toString();

		try {
			final byte[] bytes = getEcbEncryptCipher(passwd).doFinal(
					msg.getBytes());
			return urlEncode(new String(Base64.encodeBase64(bytes)));
		} catch (Exception e) {
			log.warn("encrypt failed>>>" + msg, e);
		}
		return "";
	}

	public static String decryptECB(String url, String password) {
		try {
			final byte[] debytes = getEcbDecryptCipher(password).doFinal(
					Base64.decodeBase64(urlDecode(url)));
			return new String(debytes);
		} catch (Exception e) {
			log.warn("decrypt failed>>>" + url, e);
		}
		return "";
	}

	/**
	 * 上海加密字串生成方法
	 * 
	 * @param subUserId
	 * @param ip
	 *            (采用32位二进制值)
	 * @param physicContentId
	 * @param second
	 *            (十六进制字符串)
	 * @param flag
	 * @param passwd
	 * @return
	 */
	public static String encryptSH(String subUserId, String ip,
			String physicContentId, int sec, int flag, String passwd) {

		final byte[] ipBytes = ipAddrToBytes(ip);
		final String secString = Integer.toHexString(sec);
		final String flagString = String.valueOf(flag);

		int x = 0;
		final byte[] sbytes = new byte[subUserId.length() + 2 + ipBytes.length
				+ 2 + physicContentId.length() + 2 + secString.length() + 2
				+ flagString.length()];
		x += append(sbytes, x, subUserId);
		x += append(sbytes, x, "$$");
		x += append(sbytes, x, ipBytes);
		x += append(sbytes, x, "$$");
		x += append(sbytes, x, physicContentId);
		x += append(sbytes, x, "$$");
		x += append(sbytes, x, secString);
		x += append(sbytes, x, "$$");
		x += append(sbytes, x, flagString);

		try {
			final byte[] bytes = Base64.encodeBase64(getShEncryptCipher(passwd)
					.doFinal(sbytes));
			return urlEncode(new String(bytes));
		} catch (Exception e) {
			log.warn("encrypt failed>>>" + e);
		}
		return "";
	}

	public static String decryptSH(String url, String password) {
		try {
			final byte[] bytes = Base64.decodeBase64(urlDecode(url));
			return new String(getShDecryptCipher(password).doFinal(bytes));
		} catch (Exception e) {
			log.warn("decrypt failed>>>" + url, e);
		}
		return "";
	}

	private static int append(final byte[] dest, final int destPos,
			final byte[] src) {
		final int srcLen = src.length;
		System.arraycopy(src, 0, dest, destPos, srcLen);
		return srcLen;
	}

	private static int append(final byte[] dest, final int destPos,
			final String srcStr) {
		final int srcLen = srcStr.length();
		for (int i = 0; i < srcLen; i++) {
			dest[destPos + i] = (byte) srcStr.charAt(i);
		}
		return srcLen;
	}

	/**
	 * 将点分十进制的ip转换成二进制值的形式
	 * 
	 * @param addr
	 * @return
	 */
	private static byte[] ipAddrToBytes(String addr) {
		int i = 0;
		final byte[] ipBytes = new byte[4];
		final StringTokenizer st = new StringTokenizer(addr, ".");
		while (st.hasMoreTokens() && i < 4) {
			ipBytes[i++] = (byte) Integer.parseInt(st.nextToken());
		}
		if (i != 4) {
			log.warn(String.format("Illegal IP address '%1$s'!", addr));
			return EMPTY_BYTES;
		}
		return ipBytes;
	}

	private static byte[] paddingEncryptKey(String key) {
		final byte[] encryptBytes = new byte[ENCRYPT_KEY_SIZE];
		final byte[] keyBytes = ((key != null) ? key.getBytes() : EMPTY_BYTES);
		final int keyBytesLen = Math.min(keyBytes.length, ENCRYPT_KEY_SIZE);
		int i = 0;
		while (i < keyBytesLen) {
			encryptBytes[i] = keyBytes[i];
			i++;
		}
		while (i < ENCRYPT_KEY_SIZE) {
			encryptBytes[i] = '0';
			i++;
		}
		return encryptBytes;
	}

	/**
	 * 将加密算法用到的对象保存在线程局部存储中，避免频繁创建，提高性能。
	 * <b>注意：仅适合密码不会经常变化的情景。如果是机顶盒登录认证，不适合这种算法！</b>
	 * 
	 * @author Mike Liu
	 * 
	 */
	private static final class ThreadLocalData {
		private Map<String, Cipher> shEncryptCiphers = new HashMap<String, Cipher>();
		private Map<String, Cipher> shDecryptCiphers = new HashMap<String, Cipher>();
		private Map<String, Cipher> ecbEncryptCiphers = new HashMap<String, Cipher>();
		private Map<String, Cipher> ecbDecryptCiphers = new HashMap<String, Cipher>();

		public Cipher getShEncryptCipher(final String password)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, InvalidKeyException {
			return getShCipher(shEncryptCiphers, Cipher.ENCRYPT_MODE, password);
		}

		public Cipher getShDecryptCipher(final String password)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, InvalidKeyException {
			return getShCipher(shDecryptCiphers, Cipher.DECRYPT_MODE, password);
		}

		public Cipher getEcbEncryptCipher(final String password)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, InvalidKeyException {
			return getEcbCipher(ecbEncryptCiphers, Cipher.ENCRYPT_MODE,
					password);
		}

		public Cipher getEcbDecryptCipher(final String password)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, InvalidKeyException {
			return getEcbCipher(ecbDecryptCiphers, Cipher.DECRYPT_MODE,
					password);
		}

		private static Cipher getShCipher(final Map<String, Cipher> shCiphers,
				final int opmode, final String password)
				throws InvalidKeyException, NoSuchAlgorithmException,
				NoSuchProviderException, NoSuchPaddingException {
			Cipher cipher = shCiphers.get(password);
			if (cipher == null) {
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
				cipher.init(opmode, new SecretKeySpec(
						paddingEncryptKey(password), "AES"));
				shCiphers.put(password, cipher);
			}
			return cipher;
		}

		private static Cipher getEcbCipher(
				final Map<String, Cipher> ecbCiphers, final int opmode,
				final String password) throws InvalidKeyException,
				NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException {
			Cipher cipher = ecbCiphers.get(password);
			if (cipher == null) {
				final DerivedKey dekey = OpenSSL.deriveKey(
						password.toCharArray(), null, 128, false);
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
				cipher.init(opmode, new SecretKeySpec(dekey.key, "AES"));
				ecbCiphers.put(password, cipher);
			}
			return cipher;
		}
	}

	private static final ThreadLocal<ThreadLocalData> TLDATA = new ThreadLocal<ThreadLocalData>() {
		@Override
		protected ThreadLocalData initialValue() {
			return new ThreadLocalData();
		}
	};

	private static final Cipher getShEncryptCipher(final String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return TLDATA.get().getShEncryptCipher(password);
	}

	private static final Cipher getShDecryptCipher(final String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return TLDATA.get().getShDecryptCipher(password);
	}

	private static final Cipher getEcbEncryptCipher(final String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return TLDATA.get().getEcbEncryptCipher(password);
	}

	private static final Cipher getEcbDecryptCipher(final String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		return TLDATA.get().getEcbDecryptCipher(password);
	}

	/**
	 * URL 编码(默认为UTF-8)
	 * 
	 * @param url
	 * @return 编码后的url
	 */
	public static String urlEncode(final String url) {
		if (null == url) {
			return "";
		}
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("urlEncode error, ", e);
			return url;
		}
	}

	/**
	 * URL 解码(默认为UTF-8)
	 * 
	 * @param url
	 * @return 解码后的url
	 */
	public static String urlDecode(final String url) {
		if (null == url) {
			return "";
		}
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("urlDecode error, ", e);
			return url;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String password = "1234567812345678";
		String auth2 = encryptSH("133", "128.254.0.192",
				"01000000000000000000000000000047", 0x5077d5a9, 1, password);
		System.out.println(auth2);
		String aut = "ZGbanjlLuzQT7IUwwEp9A%2FaeCSPYJH7vQX1qeJRKSzmdzJpbFRGzvgjo9WLOEukA0cbl0qxCbz%2BPpMdm8q6%2B0A%3D%3D";
		System.out.println(aut);
		String res1 = decryptSH(aut, password);
		String res2 = decryptSH(auth2, password);
		System.out.println(res1.equals(res2));

		final long count = 100000;
		final long t0 = System.currentTimeMillis();
		for (long i = 0; i < count; i++) {
			decryptSH(
					encryptSH("133", "128.254.0.192",
							"01000000000000000000000000000047", 0x5077d5a9, 1,
							password), password);
		}
		final long t1 = System.currentTimeMillis();
		long dur = t1 - t0;
		System.out.format("cost %1$d ms, %2$d times/sec.\n", dur, count * 1000
				/ dur);
		/*
		 * System.out.println(paddingEncryptKey(""));
		 * System.out.println(paddingEncryptKey("a"));
		 * System.out.println(paddingEncryptKey("aaaaaaaaaaaaaaab"));
		 * System.out.println(paddingEncryptKey("aaaaaaaaaaaaaaab123"));
		 * 
		 * System.out.println(new String(paddingEncryptKey2("")));
		 * System.out.println(new String(paddingEncryptKey2("a")));
		 * System.out.println(new
		 * String(paddingEncryptKey2("aaaaaaaaaaaaaaab")));
		 * System.out.println(new
		 * String(paddingEncryptKey2("aaaaaaaaaaaaaaab123")));
		 */
	}

}
