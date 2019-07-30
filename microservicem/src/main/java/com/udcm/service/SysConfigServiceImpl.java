/**
 * 
 */
package com.udcm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.udcm.dao.SysConfigDao;
import com.udcm.model.SysConfig;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.StringUtils;

/**
 * @Description 
 * @author hoob
 * @date 2019年5月27日下午4:11:02
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService{

	@Resource
	private SysConfigDao sysConfigDao;
	/**
	 * @Title getConfigList
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public List<SysConfig> getConfigList() {
		return sysConfigDao.getConfigList();
	}

	/**
	 * @Title findConfig
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public SysConfig findConfig(String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		return sysConfigDao.findConfig(key);
	}

	/**
	 * @Title UpdateInfo
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public SysConfig updateInfo(SysConfig sysConfig) {
		if(sysConfig==null)
			return null;
		return sysConfigDao.UpdateInfo(sysConfig);
	}

	/**
	 * @Title enableConfig
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public boolean enableConfig(String key) {
		if(StringUtils.isEmpty(key)){
			return false;
		}
		SysConfig config=sysConfigDao.findConfig(key);
		//取反
		config.setEnable(!config.getEnable());
		sysConfigDao.UpdateInfo(config);
		return true;
	}

	/**
	 * @Title reloadConfig
	 * @Description 
	 * @param 
	 * @return SysConfigService
	 * @throws 
	 */
	@Override
	public void reloadConfig() {
		//查询所有的配置
		List<SysConfig>list=sysConfigDao.getConfigList();
		if(list!=null&&list.size()>0){
			for(SysConfig co:list){
				if(co.getEnable()){
					ConfigUtil.config.put(co.getKey().toLowerCase(), co.getValue());
				}else{
					ConfigUtil.config.remove(co.getKey().toLowerCase());
				}
			}
		}
	}

}
