/**
 * 
 */
package com.udcm.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.udcm.common.Constants;
import com.udcm.model.SchedulerJob;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月28日下午4:28:08
 */
@Repository("schedulerjobDao")
public class SchedulerjobDaoImpl implements SchedulerjobDao{
	@Resource
    private MongoTemplate mongoTemplate;
	/**
	 * @Title getSchedulerJobs
	 * @Description 通过定时器分组获取定时器内容
	 * @param 
	 * @return SchedulerjobDao
	 * @throws 
	 */
	@Override
	public List<SchedulerJob> getSchedulerJobs(int group) {
		Criteria criteria = new Criteria();
		
		criteria.and("jobGroup").is(group);
		
		Query query = new Query(criteria);		
		return mongoTemplate.find(query, SchedulerJob.class, Constants.SCHEDULERJOB_COL_NAME);
	}
	/**
	 * @Title getSchedulerJobByName
	 * @Description 
	 * @param 
	 * @return SchedulerjobDao
	 * @throws 
	 */
	@Override
	public SchedulerJob getSchedulerJobByName(String name) {
		Criteria criteria = new Criteria();
	    criteria.and("jobName").is(name);
		Query query = new Query(criteria);		
		return mongoTemplate.findOne(query, SchedulerJob.class, Constants.SCHEDULERJOB_COL_NAME);
	}
	/**
	 * @Title updateInfo
	 * @Description 
	 * @param 
	 * @return SchedulerjobDao
	 * @throws 
	 */
	@Override
	public SchedulerJob updateInfo(SchedulerJob schedulerJob) {
		Criteria criteria = new Criteria();
		criteria.and("jobName").is(schedulerJob.getJobName());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("jobGroup",schedulerJob.getJobGroup());
		update.set("status",schedulerJob.getStatus());
		update.set("triggerType",schedulerJob.getTriggerType());
		update.set("cronExpression",schedulerJob.getCronExpression());
		update.set("targetObject",schedulerJob.getTargetObject());
		update.set("executTime",schedulerJob.getExecutTime());
		update.set("description",schedulerJob.getDescription());
		update.set("updateTime",new Date());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), SchedulerJob.class,Constants.SCHEDULERJOB_COL_NAME);
	}

}
