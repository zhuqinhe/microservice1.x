/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.SysConfig;
/**
 * @Description 
 * @author hoob
 * @date 2019年5月27日下午4:12:08
 */
public interface SysConfigDao {
	public List<SysConfig> getConfigList();
	public SysConfig findConfig(String key);
	public SysConfig UpdateInfo(SysConfig sysConfig); 
}
