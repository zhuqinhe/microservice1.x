/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.SchedulerJob;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月28日下午4:27:46
 */
public interface SchedulerjobDao {
List<SchedulerJob>getSchedulerJobs(int group);
SchedulerJob getSchedulerJobByName(String name);
SchedulerJob updateInfo(SchedulerJob schedulerJob);
}
