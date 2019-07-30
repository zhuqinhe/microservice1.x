package com.fonsview.metadata.utils;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import com.fonsview.metadata.config.Config;



public class Utils {
	
	private final static char[] HEXDICT = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	/**
	 * @Title processJsonUrl
	 * @Description 处理播放地址(cdn调度)
	 * @param 
	 * @return void
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	public static String processJsonUrl(HttpServletRequest request,String url,Config config) throws Exception {

		if(StringUtils.isEmpty(url)){
			return null;
		}
		// playUrl为json格式
		if (url != null && url.startsWith("{")) {
			Map<String,Object> urlMap=JsonUtils.json2Obj(url, Map.class);
			String cdn =config.getDefaultCDN();
			if (urlMap != null) {
				if(StringUtils.isNotEmpty(cdn)){
					url=urlMap.get(cdn).toString();
				}else{
					for(Object key:urlMap.keySet()){
						if(urlMap.get(key.toString())!=null){
							url=urlMap.get(key.toString()).toString();
							break;
						}
					}
				}
			}
		}
		return url;
	}

	/**
	 * @Title processJsonUrl
	 * @Description 处理播放地址(cdn调度)
	 * @param 
	 * @return void
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	public static String processJsonUrl(String clientId,String url,Config config) throws Exception {

		if(StringUtils.isEmpty(url)){
			return null;
		}
		// playUrl为json格式
		if (url != null && url.startsWith("{")) {
			Map<String,Object> urlMap=JsonUtils.json2Obj(url, Map.class);
			String cdn = config.getDefaultCDN();
			if (urlMap != null) {
				if(StringUtils.isNotEmpty(cdn)){
					url=urlMap.get(cdn).toString();
				}else{
					for(Object key:urlMap.keySet()){
						if(urlMap.get(key.toString())!=null){
							url=urlMap.get(key.toString()).toString();
							break;
						}
					}
				}
			}
		}
		return url;
	}
	/**
	 * @Title processUrlEncryptKey
	 * @Description 处理播放地址是的已经加密了
	 * @param url
	 * @param contentId
	 * @return String
	 * @throws 
	 */
	public static String processUrlEncryptKey(HttpServletRequest request,String url,String contentId,Config config) throws Exception {

		if(StringUtils.isEmpty(url)||StringUtils.isEmpty(contentId)){
			return null;
		}
		String clientIp = HttpUtils.getIpAddr(request);
		// 从AAA获取的播放地址可能已经带了防盗链
		if (!url.contains("AuthInfo=")){
			// 添加防盗链
			if (StringUtils.isNotEmpty(config.getPassportkey())) {
				int sec_start = (int) (System.currentTimeMillis() / 1000);
				String authStr = AESSecurity.encryptSH("userId", clientIp,contentId, sec_start, 1, config.getPassportkey());
				String splitChar = "?";
				if (url.contains("?")) {
					splitChar = "&";
				}
				int encryptKey_version=1;
				String encryptKey_version_tmp=config.getVersion();
				if(StringUtils.isNotEmpty(encryptKey_version_tmp)){
					try {
						encryptKey_version=Integer.parseInt(encryptKey_version_tmp);
					} catch (Exception e) {
						encryptKey_version=0;
					}
				}

				url = url + splitChar + "AuthInfo=" + authStr+ "&version=" + encryptKey_version;
			}
			return url;
		}
		return url;
	}
	/**
	 * @Title processUrlEncryptKey
	 * @Description 处理播放地址是的已经加密了(上海)
	 * @param url
	 * @param contentId
	 * @return String
	 * @throws 
	 */
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
}
