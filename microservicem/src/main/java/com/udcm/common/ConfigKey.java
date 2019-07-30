package com.udcm.common;

/**
 * 
 * 配置KEY
 * 
 * @author Faker
 * 
 */
public enum ConfigKey {
	//请求超时时间
	TCPTIMEOUT,
	//只获取几个月的记录
	LIMITMONTH,
	//是否需要翻译虚拟ID
	TRANSLATE,
	//epg服务器的地址
	EPGSERVERURL,
	//
	SYNCVIRTUALIDTIMERCHANNEL,
	//同步虚拟id的时间配置
	SYNCVIRTUALIDTIMERSERIES,
	//同步虚拟id的时间配置
	SYNCVIRTUALIDTIMERPROGRAM,
	//区分是哪个现场的配置
	SCENE,
	//tomcat 的零时根目录
	TOMCATTEMURL,
	//访问日志格式配置
	ACCESSLOGPATTERN,
	//是否开启访问日志
	ACCESSLOGENABLED,
	//tomcat最大连接数配置
	TOMCATCONNECTIONS,
	//tomcat最大线程数配置
	TOMCATMAXTHREADS,
	//tomcat最大线程数配置
	TOMCATMCONNECTIONTIMEOUT,
	//最小线程数
	TOMCATMINSPARETHREADS,
	//可用的  参考值 CPU 8个以下逻辑核心  acceptorThreadCount =2   ，CPU 8个以上逻辑核心  acceptorThreadCount = 4
	TOMCATACCEPTORTHREADCOUNT,
	//
	TOMCATACCEPTCOUNT,
	//
	TOMCATMAXIDLETIME,
	//
	MAXKEEPALIVEREQUESTS,
	//
	KEEPALIVETIMEOUT,
	//平台配置
	PLATFORM,
	//获取芒果最新收藏
	FAVORITEURL,
	//获取芒果最新书签
	BOOKMARKURL,
	//同一个合集下只能有一个最新的书签，还是可有有多个
	BOOKMARKPROGRAMSINGLE,
	//同一个合集下返回多个分集还是最新的一个
	BOOKMARKRETURNSINGLE,
	//是否检验MAC
	VERIFYHMAC,
	//收藏数量限制
	FAVORITESIZE,
	//书签数量限制
	BOOKMARKSIZE,
	//收藏队列名称
	FAVORITEQUEUE,
	//书签队列名称
	BOOKMARKQUEUE,
	//默认语言配置
	DEFAULT_LANGUAGE,
	//图片地址前缀，对310有效，cms310保存的是相对地址
	PICTURE_URL_PREFIX,
	//图片需要被替换的地址，cms300中图片是绝对地址
	PICTURE_URL_PREFIX_OLD,
	//图片需要被替换成的地址，cms300中图片是绝对地址
	PICTURE_URL_PREFIX_NEW,
	//定时清理超量数据
	DELETEOVERSIZETIMER,
	//书签限制时长
	BOOKMARKLIMITTIME,
	//是否启用定时器删除用户数据
	TIMERDELETE,
	//ui 界面系统语种
	LANGS_CONFIG
}

