/**
 * 
 */
package com.fonsview.metadata.constants;

import java.util.HashMap;
import java.util.Map;


public class Constants {
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
	
	/***点播分类类型*/
	public static final int VOD_TYPE = 0;
	/***直播分类类型*/
	public static final int LIVE_TYPE=1;
	/**epg缓存池大小，超出后 按淘汰策略淘汰***/
	public static final int cachesize=50000;
	/****epg缓存过期时间*****/
	public static final int cachetimeout=30;
	

	public static Map<String, String> adContentTypeMap = new HashMap<String, String>();
	static {
		adContentTypeMap.put("1", "category");
		adContentTypeMap.put("2", "2");
		adContentTypeMap.put("3", "2");
		adContentTypeMap.put("4", "3");
		adContentTypeMap.put("5", "apk");
		adContentTypeMap.put("6", "link");
		adContentTypeMap.put("7", "8");
		adContentTypeMap.put("8", "categoryIdentifier");
		adContentTypeMap.put("9", "22");
		adContentTypeMap.put("10", "7");
		adContentTypeMap.put("11", "21");
		adContentTypeMap.put("12", "ad");
		adContentTypeMap.put("13", "fenji");
		adContentTypeMap.put("14", "liveshow");
		adContentTypeMap.put("15", "specialTopic");
		adContentTypeMap.put("18", "star");
	}
	
	public static final String AD_CONTENT_CATEGORY = "1";
	public static final String AD_CONTENT_VOD = "2";
	public static final String AD_CONTENT_APK = "5";
	public static final String AD_CONTENT_ACTIVITY = "10";
	public static final String AD_CONTENT_LINK = "6";
	public static final String AD_CONTENT_STAR = "18";
	
	public final static int MOD_VOD = 1; // 点播
	public final static int MOD_LIVE = 2; // 直播
	public final static int MOD_MIXED = 3; // 都有
	
	//
	public final static int RECYCLE_0 = 0; // 正常
	public final static int RECYCLE_1 = 1; // 回收
	
	/**
	 * MiniMetadata 不同mediaType对应的表名
	 */
	public static final String SERIES = "series";
	public static final String PROGRAM = "program";
	public static final String MOVIE = "movie";
	public static final String CHANNEL = "channel";
	public static final String CAST = "cast";
	public static final String APP = "app";
	public static final String CATEGORY = "category";
	public static final String SeriesProgram="series_program";
	public static final String ProgramMovie="program_movie";
	public static final String CategoryAssociation="category_association";
	public static final String CastAssociation="cast_association";
	public static final String FileResource="file_resource";
	public static final String SCHEDULE = "schedulerecord";
	public static final String SERVICE = "service";
	public static final String ServiceAssociation = "service_association";
	

	/**
	 * 频道是那种协议开始的
	 * **/
	public static final String URL_HEAD_HTTP = "http://";
	public static final String URL_HEAD_RTSP = "rtsp://";
	
	public static final String R_SUCCESS="SUCCESS";
	public static final String R_FAIL="FAIL";
	
	public static final String DEFAULT_PAGESIZE = "8";
	public static final String DEFAULT_PAGE = "1";
	public static final String TIME_REGEX_yyyyMMddHHmmss = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229))([0-1]?[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";
	
	/**
	 * 系统配置类型：系统配置
	 */
	public static String SYS_CONFIG_TYPE_CONFIG = "config";

	/**
	 * 系统配置类型：系统常量
	 */
	public static String SYS_CONFIG_TYPE_CONSTANT = "constant";

	/**
	 * 系统配置类型：注入配置
	 */
	public static String SYS_CONFIG_TYPE_INJECT = "inject";

	/**
	 * 系统配置类型：分发配置
	 */
	public static String SYS_CONFIG_TYPE_DIS = "dis";
	/**
	 * 系统配置类型：图片配置
	 */
	public static String SYS_CONFIG_TYPE_PIC = "pic";
	
	public static final String SP_CONTENTID = "SP_CATEGORY";
	
	   /**
     * VERSION_ASSOCIATETYPE：EXAMPLE 0
     */
    public static int VERSION_ASSOCIATE_TYPE_EXAMPLE = 0;

    /**
     * VERSION_ASSOCIATETYPE：GLOBAL 1
     */
    public static int VERSION_ASSOCIATE_TYPE_GLOBAL = 1;

    /**
     * VERSION_ASSOCIATETYPE：STRATEGY 2
     */
    public static int VERSION_ASSOCIATE_TYPE_STRATEGY = 2;

    /**
     * VERSION_ASSOCIATETYPE：SCREENSAVER 3
     */
    public static int VERSION_ASSOCIATE_TYPE_SCREENSAVER = 3;
    /**
     * VERSION_ASSOCIATETYPE：MESSAGE 4
     */
    public static int VERSION_ASSOCIATE_TYPE_IMAGE_INTRODUCTION = 4;
    /**
     * VERSION_ASSOCIATETYPE：MESSAGE 5
     */
    public static int VERSION_ASSOCIATE_TYPE_MESSAGE = 5;

    /**
     * 首页配置
     */
    public static String EPG_EXAMPLE_HOME = "home";
    /**
     * 自定义专题类型
     */
    public static String EPG_EXAMPLE_SPECIAL = "special";

    public static  String EPG_VERSION_TYPE_WEB = "web" ;

    public static  String EPG_VERSION_TYPE_APK = "apk" ;
    
    
	

}
