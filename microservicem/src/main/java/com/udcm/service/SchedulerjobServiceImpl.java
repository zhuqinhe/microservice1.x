/**
 * 
 */
package com.udcm.service;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.udcm.constants.SchedulerJobType;
import com.udcm.dao.SchedulerjobDao;
import com.udcm.model.SchedulerJob;
import com.udcm.utils.StringUtils;



/**
 * @Description 
 * @author hoob
 * @date 2019年5月28日下午4:28:52
 */
@Service("schedulerjobService")
public class SchedulerjobServiceImpl implements SchedulerjobService{
	
	private Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

	@Resource
	private Scheduler scheduler;
	@Resource
    private SchedulerjobDao schedulerjobDao;

	/**
	 * @Title getSchedulerJobs
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public List<SchedulerJob> getSchedulerJobs(int group) {
		return schedulerjobDao.getSchedulerJobs(group);
	}

	/**
	 * @Title getSchedulerJobByName
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public SchedulerJob getSchedulerJobByName(String name) {
		if(StringUtils.isEmpty(name)){
			return null;
		}
		return schedulerjobDao.getSchedulerJobByName(name);
	}

	/**
	 * @Title updateInfo
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public SchedulerJob updateInfo(SchedulerJob schedulerJob) {
		if(schedulerJob==null){
			return null;
		}
		return schedulerjobDao.updateInfo(schedulerJob);
	}

	/**
	 * @Title toEnable
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public boolean toEnable(String name) {
		if(StringUtils.isEmpty(name)){
			return false;
		}
		try {
			SchedulerJob job=schedulerjobDao.getSchedulerJobByName(name);
			job.setStatus(!job.getStatus());
			if(job.getStatus()){
				start(job);
			}else{
				stop(job);
			}
			schedulerjobDao.updateInfo(job);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return true;
	}

	/**
	 * @throws ClassNotFoundException 
	 * @throws SchedulerException 
	 * @Title start
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public boolean start(SchedulerJob job) throws Exception {
		if (null == job) {
            return false;
        }

		// 如果job的状态为禁用，则不启动
		if (job.getStatus() == false) {
			logger.info("schedule job status is disabled,can not need to start");
			return true;
		}

		if (job.getTriggerType() == SchedulerJobType.CRON.getValue()) {
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), String.valueOf(job.getJobGroup()));
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (null == trigger) {
				String className = job.getTargetObject();
				@SuppressWarnings("unchecked")
				Class<Job> cl = (Class<Job>) Class.forName(className);

				JobDetail jobDetail = JobBuilder.newJob(cl)
						.withIdentity(job.getJobName(), String.valueOf(job.getJobGroup())).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);

				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), String.valueOf(job.getJobGroup()))
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				logger.info("job(" + job.toString() + ") started success");
			} else {// Trigger已存在，那么更新相应的定时设置
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				logger.info("job(" + job.toString() + ") is exist,now update is and restarted success");
			}
			return true;
		} else {
			logger.error("unsuport trigger start job(" + job.toString() + ") fail");
		}
		return false;
	}

	/**
	 * @throws Exception 
	 * @Title stop
	 * @Description 
	 * @param 
	 * @return SchedulerjobService
	 * @throws 
	 */
	@Override
	public boolean stop(SchedulerJob job) throws Exception {
		if (null == job) {
			logger.error("job is null");
			return false;
		}
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), String.valueOf(job.getJobGroup()));
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (SchedulerJobType.CRON.getValue() == job.getTriggerType()) {
			if (null != trigger) {
				scheduler.deleteJob(trigger.getJobKey());
				logger.info("stop job  successfully");
			} else {
				logger.warn("can not get tigger cron  by jobName(" + job.getJobName() + ") and jobGroup("
						+ job.getJobGroup() + ")");
			}
			return true;
		} else {
			logger.error("unsuport trigger stop job(" + job.toString() + ") fail");
		}

		return false;
	}

}
