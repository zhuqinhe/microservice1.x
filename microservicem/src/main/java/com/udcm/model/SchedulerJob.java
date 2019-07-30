package com.udcm.model;


import java.io.Serializable;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "schedulerJob")
public abstract class SchedulerJob implements Job,Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Indexed(name="jobName", unique=true, dropDups=false)
	private String jobName;//任务名称
	private Integer jobGroup;//任务所属分组
	private Boolean status;//任务状态 :false为禁用,true为启用 
	private Integer triggerType;// 触发类型,可取值:cron、simple、calendar
	private String cronExpression;//任务运行时间表达式
	private String targetObject;//jobDetail类似全名
	private String description;  //描述
	private String executTime;	 //执行时间
	private Date updateTime;
	public String getJobName() {
		return jobName;
	}
	public Integer getJobGroup() {
		return jobGroup;
	}
	public Boolean getStatus() {
		return status;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public String getTargetObject() {
		return targetObject;
	}
	public String getDescription() {
		return description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public void setJobGroup(Integer jobGroup) {
		this.jobGroup = jobGroup;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getExecutTime() {
		return executTime;
	}
	public void setExecutTime(String executTime) {
		this.executTime = executTime;
	}
	
}
