package com.udcm.common;

import java.util.HashMap;
import java.util.Map;



public class Constants {

	public static String SYNC_DATA_ETC_PATH = "/opt/fonsview/NE/udc/favorite/etc/";
	
	/**
	 * 书签收藏集合名称
	 */
	public static String BOOKMARK_COL_NAME = "bookmark_collection";

	/**
	 * 用户点播收藏集合名称
	 */
	public static String FAVORITE_COL_NAME = "favorite_collection";
	
	/***
	 * 用户盒子的绑定信息
	 * ***/
	public static String STB_USER_COL_NAME = "stb_user_collection";
	

	/***
	 * 需要删除数据的表
	 * ***/
	public static String NEEDTODELETE_COL_NAME = "needtodelete_collection";
	
	/***
	 * 用户信息集合
	 * ***/
	public static String USER_COL_NAME="user";
	
	/***
	 * 系统配置集合
	 * ***/
	public static String SYS_CONFIG_COL_NAME="sys_config";
	
	/***
	 * 记录定时器的一些相关信息
	 * ***/	
	public static String SCHEDULERJOB_COL_NAME="schedulerJob";
	
	
	/**
	 * 用户评分集合
	 */
	public static String SCORE_COL_NAME = "score_collection";
	
	/**
	 * 用户踩/赞集合
	 */
	public static String RECOMMENDATION_COL_NAME = "recommendation_collection";
	
	/**
	 * 用户评论集合
	 */
	public static String COMMENT_COL_NAME = "comment_collection";
	
	/**
	 * 用户提醒集合
	 */
	public static String REMINDER_COL_NAME = "reminder_collection";
	
	/**
	 * 内容评分集合
	 */
	public static String CONTENTSCORE_COL_NAME = "contentscore_collection";
	
	/**
	 * 内容踩/赞统计集合
	 */
	public static String CONTENTRECOMMENDATION_COL_NAME = "contentrecommendation_collection";
	
	/***
	 * 内容翻译表
	 * ***/
	public static String MEDIADATA_COL_NAME = "mediadata_collection";
	
	
	
	// 成功
	public static final int SUCCESS = 0;
	// 部分成功
	public static final int PART_SUCCESS = 1;
	// 失败
	public static final int FAILURE = -1;
	// UserToken无效
	public static final int USERTOKEN_INVALID = 2011;
	// UserToken已经过期
	public static final int USERTOKEN_EXPIRED = 2012;
	// 认证失败
	public static final int VERIFY_FAILURE = 2201;
	// 无认证字符串
	public static final int NO_VERIFY_STRING = 2205;
	// 无认证字符串
	public static final int REQUIRED_PARAMETER_MISSING = 4001;
	public static final String REQUIRED_PARAMETER_MISSING_MSG = "Required parameter is missing from request.";
	// 服务端异常
	public static final int SERVER_EXCEPTION = 5000;
    public static final String SERVER_EXCEPTION_DESCRIPTION="Server Exception";
    public static final String AUTHENTICATION_FAILED="Authentication failed";
    
    public static final int ACTION_ADD=0;
    public static final int ACTION_DELETE=1;
    public static final String DEFAULT_MEDIATYPE="2";

	public static Map<Integer, String> descriptionMap = new HashMap<Integer, String>();
	//public static Map<String, String> programMap = new HashMap<String, String>();  一级分类ID转换
	static {
		descriptionMap.put(SUCCESS, "Success");
		descriptionMap.put(PART_SUCCESS, "Part success");
		descriptionMap.put(FAILURE, "failure");
		descriptionMap.put(USERTOKEN_INVALID, "Usertoken invalid");
		descriptionMap.put(USERTOKEN_EXPIRED, "Usertoken expired");
		descriptionMap.put(VERIFY_FAILURE, "Authentication failed");
		descriptionMap.put(NO_VERIFY_STRING, "No authentication string");
		descriptionMap.put(SERVER_EXCEPTION, "Server exception");
		descriptionMap.put(2, "Authentication failed");
		descriptionMap.put(-3000, "Server exception");
	
		/*programMap.put("31","1120");
		programMap.put("27","1125");
		programMap.put("910","3001");
		programMap.put("28","1119");
		programMap.put("21","1200");
		programMap.put("26","1114");
		programMap.put("33","1115");
		programMap.put("22","1116");
		programMap.put("30","1101");
		programMap.put("11","活动");
		programMap.put("34","游戏");
		programMap.put("25","1117");
		programMap.put("6", "1000");
		programMap.put("5","1001");
		programMap.put("17","1118");
		programMap.put("19","1107");
		programMap.put("24","1102");
		programMap.put("9","1114");
		programMap.put("18","1108");*/
	
		
	}
	
	public static final int PAGESIZE=30;
	public static final int BEGIN=0;
    public static final String SOURCE_TYPE_FONSVIEW="fonsview";
    public static final String SOURCE_TYPE_OTHER="other";
}
