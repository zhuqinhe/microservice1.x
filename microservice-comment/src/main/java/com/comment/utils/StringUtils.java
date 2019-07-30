package com.comment.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;


/**
 * String Tools Class
 */
public final class StringUtils {
	
	public static final long HOURS_ONE_DAY = 24 * 60 * 60 * 1000;
	private static Logger log = Logger.getLogger(StringUtils.class);

	/**
	 * 人文件的作路径中
	 * 
	 * @param path
	 * @return
	 */
	public static String getDirFromPath(String path) {
		if (path == null || "".equals(path.trim())) {
			return null;
		}
		int index = path.lastIndexOf("/");
		if (index >= 0) {
			return path.substring(0, index + 1);
		}
		return null;
	}

	/**
	 * 从文件名全路径中获取文件名称
	 * 
	 * @param path
	 * @return 说明：路径分隔符必须为/
	 */
	public static String getFileNameFromPath(String path) {
		if (path == null || "".equals(path.trim())) {
			return null;
		}
		int index = path.lastIndexOf("/");
		if (index >= 0) {
			return path.substring(index + 1);
		}
		return null;
	}

	/**
	 * 判断参数是否为空：此处为空的条件是参数等于null或空串或null字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean paramIsNull(String param) {
		if (null == param || "".equals(param)
				|| "null".equalsIgnoreCase(param.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断参数是否不为空：此处为空的条件是参数等于null或空串或null字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean paramIsNotNull(String param) {
		return !paramIsNull(param);
	}
	public static String handleStrParam(String p) {
		if (p == null || p.trim().length() <= 0
				|| "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			return null;
		}
		return p.trim();
	}

	public static Integer handleIntParam(String p) {
		Integer i = null;
		if (p == null || p.trim().length() <= 0
				|| "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			return null;
		}
		try {
			i = Integer.parseInt(p);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			log.error("分页数字转换错误");
		}
		return i;
	}

	public static Long handleLongParam(String p) {
		Long pl = null;
		if (p == null || p.trim().length() <= 0
				|| "null".equalsIgnoreCase(p.trim())
				|| "undefined".equalsIgnoreCase(p.trim())) {
			pl = null;
		} else {
			try {
				pl = Long.parseLong(p);
			} catch (NumberFormatException e) {
				log.error("id is not a number");
				log.error(e.getMessage(), e);
			}
		}
		return pl;
	}

	public static String set2Str(Set<String> set, String separator) {
		if (set == null || set.size() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (String s : set) {
			if ("".equals(sb.toString())) {
				sb.append(s);
			} else {
				sb.append(separator);
				sb.append(s);
			}
		}
		return sb.toString();
	}

	/**
	 * 用指定的分隔符将list转换成String
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String list2Str(List list, final String separator) {
		if (list == null || list.size() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (Object obj : list) {
			if ("".equals(sb.toString())) {
				sb.append(obj.toString());
			} else {
				sb.append(separator);
				sb.append(obj.toString());
			}
		}
		return sb.toString();
	}
	
	/**
	 * 用指定的分隔符将string转换成list
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static List<String> str2List(String str, final String separator) {
		List<String> list = new ArrayList<String>();
		if(isNotEmpty(str)){
			String[] strArray = str.split(separator);
			if(strArray!= null && strArray.length > 0){
				for (int i = 0; i < strArray.length; i++) {
					list.add(strArray[i]);
				}
			}
		}
		return list;
	}

	/**
	 * 判断字符串是否为大于0的数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumberAndMoreThanZeo(String s) {
		if ( org.apache.commons.lang3.StringUtils.isNumeric(s) && Long.parseLong(s) > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(CharSequence  s) {
		return org.apache.commons.lang3.StringUtils.isNumeric(s);
	}

	public static final boolean isEmpty(CharSequence str) {
		return org.apache.commons.lang3.StringUtils.isBlank(str);
	}

	public static final boolean isNotEmpty(CharSequence str) {
		return org.apache.commons.lang3.StringUtils.isNotBlank(str);
	}

	/**
	 * 如果s不为空（去除前后空白后），则返回前后加“%”的串，否则，返回null。 用于在数据中匹配字符串字段。
	 * 
	 * @param s
	 *            源字符串。
	 * @return 如果s不为空（去除前后空白后），则返回前后加“%”的串，否则，返回null。
	 */
	public static final String likeStr(String s) {
		s = emptyToNull(s);
		if (s != null) {
			s = s.replace("%", "\\%");
			s = s.replace("_", "\\_");
			return '%' + s + '%';
		} else {
			return null;
		}
	}

	/**
	 * 用于匹配s开头的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static final String likeSearchStr(String s) {
		s = emptyToNull(s);
		if (s != null) {
			s = s.replace("%", "\\%");
			s = s.replace("_", "\\_");
			return s + '%';
		} else {
			return null;
		}
	}

	/**
	 * 如果s去除前后空白后为空，则返回null；否则返回s去除前后空白后的串。
	 * 
	 * @param s
	 *            源字符串。
	 * @return 如果s去除前后空白后为空，则返回null；否则返回s去除前后空白后的串。
	 */
	public static final String emptyToNull(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		} else {
			if (StringUtils.isEmpty(s = s.trim())) {
				return null;
			} else {
				return s;
			}
		}
	}

	// set转string
	public static final String set2String(Set<Long> cp_ids) {
		if (cp_ids != null && cp_ids.size() >=1) {
			StringBuilder sb = new StringBuilder();
			for (Long id : cp_ids) {
				if (sb.toString().length() > 0) {
					sb.append("," + id);
				} else {
					sb.append(id);
				}
			}
			return sb.toString();
		} else {
			return null;
		}

	}

	public static List<String> getStrListBySeperator(String str,
			String seperator) {
		List<String> strList = new ArrayList<String>();
		if (str == null || str.equals("")) {
			return strList;
		}
		strList = Arrays.asList(str.split(seperator));
		return strList;
	}

	/**
	 * 判断输入字符串是否为null，为null则返回""
	 */
	public static String formatNullStr(Object str) {
		return str == null ? "" : str.toString();
	}

	public static Integer toInteger(Object o, Integer def) {
		if (o == null) {
			return def;
		} else if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof String
				&& ((String) o).trim().matches("^-?\\d+$")) {
			return Integer.valueOf((String) o);
		} else {
			return def;
		}
	}

	public static String isRightfulDate_regex = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";

	public static boolean isRightfulDate(String date) {
		return date.matches(isRightfulDate_regex);
	}

	public static String isRightfulTime_regex = "^([0-1]{1}[0-9]{1}|2[0-3]{1}):[0-5]{1}[0-9]{1}$";

	public static boolean isRightfulTime(String date) {
		return date.matches(isRightfulTime_regex);
	}

	/**
	 * 处理语言字符串，方便查询。
	 * 
	 * @param lang
	 *            必须是如"zh-cn"、"en-us"格式。
	 * @return 返回"zh_CN"、"en_US"格式的字符串。
	 */
	public static String handleLangStr(String lang) {
		return new StringBuilder(lang.substring(0, lang.indexOf("-"))).append("_")
				.append(lang.substring(lang.indexOf("-") + 1).toUpperCase()).toString();
	}

	public static String getCorrelateID() {
		String correlateID = UUID.randomUUID().toString().replace("-", "");
		if (correlateID.length() != 32) {
			log.info(">>>>>correlateID is not 32bit");
		}
		return correlateID;
	}
	
	/**
	 * 转换url里的'特殊字符
	 * 
	 * @param url
	 * @return
	 */
	public static String formatUrl(String url){
		return url == null ? "" : url.replace("'", "\"");
	}
	
	/**
	 * 转义内容中带有'特殊字符
	 * 
	 * @param value
	 * @return
	 */
	public static String formatSpecialChar(String value){
		return value == null ? "" : value.replace("\'", "\\'");
	}
	
	public static String getProtocolType(String domain) {
		if (StringUtils.isNotEmpty(domain)) {
			return domain.substring(domain.length() - 1);
		}
		return null;
	}
	

	public static String getCorrelateID4UNICOM() {
		return getCorrelateID().substring(0, 20);
	}
	
	
	public static String getUUID32(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
