/**
 * 
 */
package com.fonsview.metadata.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description 
 * @author hoob
 * @date 2019年7月19日下午5:33:51
 */
public class MapUtils {
	/**
	 * 栏目编号和contentId的对应关系及contentId和编号的对应关系
	 */
	public static Map<String, String> IDENTITYNOORCONTENTID = new ConcurrentHashMap<String,String>();
	/**
	 * 栏目编号和标识的对应关系
	 */
	public static Map<String, String>CONTENTIDORIDENTIFER= new ConcurrentHashMap<String,String>();

}
