/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.SchedulerJob;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月28日下午4:28:40
 */
public interface SchedulerjobService {
	List<SchedulerJob>getSchedulerJobs(int group);
	SchedulerJob getSchedulerJobByName(String name);
	SchedulerJob updateInfo(SchedulerJob schedulerJob);
	boolean toEnable(String name);
	boolean start(SchedulerJob job)throws Exception;
	boolean stop(SchedulerJob job)throws Exception;
}
