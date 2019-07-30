package com.fonsview.sync.common;

/**
 * 
 * 配置KEY
 * 
 * @author Faker
 * 
 */
public enum ConfigKey {
	CMSVERSION,//cms的版本  是300还是cms310
	DEFAULT_LANGUAGE,//默认语言配置
	PICTURE_URL_PREFIX,//图片地址前缀，对310有效，cms310保存的是相对地址
	PICTURE_URL_PREFIX_OLD,//图片需要被替换的地址，cms300中图片是绝对地址
	PICTURE_URL_PREFIX_NEW //图片需要被替换成的地址，cms300中图片是绝对地址
}
