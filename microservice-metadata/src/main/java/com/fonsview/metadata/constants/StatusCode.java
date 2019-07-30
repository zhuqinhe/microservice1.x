package com.fonsview.metadata.constants;

/**
 * 封装各模块采用的状态码，便于运营人员快速定位问题
 * @author Raul	
 * 2017年7月4日
 */
public class StatusCode {	
	
	
	public static class UI{
		
//		0	成功
//		-1	失败
//		20000	Service Currently Unavailable			
//		20001	Invalid Auth Token
//		20002	Auth Token Timeout
//		20003	Invalid Captcha
//		20004	Captcha Timeout
//		20005	Unauthorized
//		20006	Partially Authorized Content
//		20008	Invalid User
//		20009	Abnormal Operation
//		20010	Wrong Arguments
//		20011	IP Restrict
//		20012   Unable user
//		40001	Missing Required Arguments
//		40002	Invalid Arguments
		
		public static final int UI_0 = 0;	
		public static final int UI_1= -1; //未登录
		public static final int UI_20001 = 20001;
		public static final int UI_20002 = 20002;
		public static final int UI_20003 = 20003;
		public static final int UI_20004 = 20004;
		public static final int UI_20005 = 20005;
		public static final int UI_20006 = 20006;
		public static final int UI_20008 = 20008;
		public static final int UI_20009 = 20009;
		public static final int UI_20010 = 20010;
		public static final int UI_20011 = 20011;
		public static final int UI_20012 = 20012;
		public static final int UI_40001 = 40001;
		public static final int UI_40002 = 40002;
		public static final int UI_OLD_PASWORD_ERROR = 400;
		public static final int UI_VOD_50001 = 50001;//垫片不能删除
		public static final int UI_VODTYPE_50002 = 50002;//已关联点播不能删除
		public static final int UI_VODTYPE_50003 = 50003;//已关联直播不能删除
		public static final int UI_PWD_60001 = 60001;//密码过期
		public static final int ERROR_SYS_CONFIG_CHECK = 102001;//"SysConfig check 异常";
		public static final int ERROR_TCGS_VOD_PARSE_CODE = 102001000;//"TCGS_VOD配置解析错误";		
		public static final int ERROR_TVGW_VOD_PARSE_CODE = 102001001;// "TVGW_VOD配置解析错误";		
		public static final int ERROR_TVGW_LIVE_PARSE_CODE = 102001002; //"TVGW_LIVE配置解析错误";		
		public static final int ERROR_DRM_PARSE_CODE = 102001003; //"DRM分发配置解析错误";		
		public static final int ERROR_CDN_PARSE_CODE = 102001004; //"CDN分发配置解析错误";		
		public static final int ERROR_CMS_PARSE_CODE = 102001005; // "CMS分发配置解析错误";		
		public static final int ERROR_BMS_PARSE_CODE = 102001006; //"BMS分发配置解析错误";		
		public static final int ERROR_EPG_PARSE_CODE = 102001007; //"EPG分发配置解析错误";		
		public static final int ERROR_EPGTEM_PARSE_CODE = 102001008;//"EPG模板分发配置解析错误";
		
		
	}
	
	/**
	 * 0 成功
	 * 
	 * -1 失败
	 * 
	 * @author Faker
	 *
	 */
	public static class C1 {
		/**
		 * 成功
		 */
		public static final int C1_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int C1_FAIL = -1;

	}
	
	/**
	 * 0 成功过
	 * 
	 * -1 失败
	 * 
	 * @author Faker
	 *
	 */
	public static class C2{
		/**
		 * 成功
		 */
		public static final int C2_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int C2_FAIL = -1;

	}
	
	public static class CE{
		/**
		 * 成功
		 */
		public static final int CE_SUCCESS = 0;
		/**
		 * 失败
		 */
		public static final int CE_FAIL = -1;

	}
}
