/**
 * 
 */
package com.udcm.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.udcm.common.Constants;
import com.udcm.model.SysConfig;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月27日下午4:12:25
 */
@Repository("sysConfigDao")
public class SysConfigDaoImpl implements SysConfigDao{
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	/**
	 * @Title getConfigList
	 * @Description 
	 * @param 
	 * @return SysConfigDao
	 * @throws 
	 */
	@Override
	public List<SysConfig> getConfigList() {
		//查询条件
		Criteria criteria = new Criteria();
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"createTime")));
		return mongoTemplate.find(query, SysConfig.class, Constants.SYS_CONFIG_COL_NAME);
	}

	/**
	 * @Title findConfig
	 * @Description 
	 * @param 
	 * @return SysConfigDao
	 * @throws 
	 */
	@Override
	public SysConfig findConfig(String key) {
		Criteria criteria = new Criteria();
		criteria.and("key").is(key);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, SysConfig.class, Constants.SYS_CONFIG_COL_NAME);
	}

	/**
	 * @Title UpdateInfo
	 * @Description 
	 * @param 
	 * @return SysConfigDao
	 * @throws 
	 */
	@Override
	public SysConfig UpdateInfo(SysConfig sysconfig) {
		Criteria criteria = new Criteria();
		criteria.and("key").is(sysconfig.getKey());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("value",sysconfig.getValue());
		update.set("enable",sysconfig.getEnable());
		update.set("name", sysconfig.getName());
		update.set("updateTime",new Date());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), SysConfig.class,Constants.SYS_CONFIG_COL_NAME);
		
	}

}
