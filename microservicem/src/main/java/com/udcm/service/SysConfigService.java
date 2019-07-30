/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.SysConfig;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月27日下午4:10:41
 */
public interface SysConfigService {
	public List<SysConfig> getConfigList();
	public SysConfig findConfig(String key);
	public SysConfig updateInfo(SysConfig sysConfig);
	public boolean enableConfig(String key);
	
	/**
	 * @Title reloadConfig
	 * @Description 定时加载db里面的配置
	 * @param 
	 * @return void
	 * @throws 
	 */
	public void reloadConfig();
	
}
