package com.udcm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udcm.constants.StatusCode;
import com.udcm.model.SchedulerJob;
import com.udcm.service.SchedulerjobService;
import com.udcm.utils.StringUtils;
import com.udcm.vo.ListResponse;
import com.udcm.vo.Response;
import com.udcm.vo.VoResponse;



@RestController
@RequestMapping("/ui")
public class SchedulerJobController {
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	@Resource
	private	SchedulerjobService schedulerJobService;

	@RequestMapping(method=RequestMethod.GET,path="/v1/sys/schdljobmng/list", produces={"application/json"})
	public ListResponse<SchedulerJob> getSchedulerJobList(@RequestParam("jobgroup") String jobgroup){
		// logger.debug("get SchedulerJob list ");
		ListResponse<SchedulerJob> response = new ListResponse<SchedulerJob>(StatusCode.UI.UI_0);
		try {
			int jobgroup_int=StringUtils.obj2Int(jobgroup, 1);
			List<SchedulerJob> schedulerJobList = schedulerJobService.getSchedulerJobs(jobgroup_int);
			response.setList(schedulerJobList);
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription("get SchedulerJob list error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.GET,path="/v1/sys/schdljobmng/detail/{id}", produces={"application/json"})
	public VoResponse<SchedulerJob> getSchedulerJob(@PathVariable("id") String id){
		// logger.debug("get SchedulerJob  ");
		VoResponse<SchedulerJob> response = new VoResponse<SchedulerJob>();
		try {
			id=StringUtils.handleStrParam(id);
			SchedulerJob schedulerJobVO=schedulerJobService.getSchedulerJobByName(id);
			response.setVo(schedulerJobVO);
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription("get SchedulerJob  error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.POST,path="/v1/sys/schdljobmng/enable/{id}", produces={"application/json"})
	public VoResponse toEnable(@PathVariable("id") String id){
		// logger.debug("SchedulerJob toEnable  ");
		VoResponse response = new VoResponse();
		try {
			id=StringUtils.handleStrParam(id);
			schedulerJobService.toEnable(id);
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription(" SchedulerJob toEnable error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.PUT,path="/v1/sys/schdljobmng/edit",consumes={"application/json"}, produces={"application/json"})
	public Response updateScheduler(@RequestBody SchedulerJob schedulerJob){
		// logger.debug("updateScheduler  ");
		    Response response = new Response();

		try {
			SchedulerJob job=schedulerJobService.getSchedulerJobByName(schedulerJob.getJobName());
			if(job==null){
				response.setResultCode(-1);
				response.setDescription(" SchedulerJob is null");
			}
			else{
				/*schedulerJobVO.setCreateTime(schedulerJob.getCreateTime());
        		schedulerJobVO.setTargetObject(schedulerJob.getTargetObject());
        		schedulerJobVO.setJobGroup(schedulerJob.getJobGroup());;
        		schedulerJobVO.setId(schedulerJob.getId());
        		schedulerJobVO.setUpdateTime(new Date());
        		schedulerJobVO.setStatus(schedulerJob.getStatus());
        		schedulerJobVO.setJobName(schedulerJob.getJobName());
        		schedulerJobVO.setTriggerType(schedulerJob.getTriggerType());*/
				job.setCronExpression(schedulerJob.getCronExpression());
				job.setDescription(schedulerJob.getDescription());
				job.setJobGroup(schedulerJob.getJobGroup());
				job.setStatus(schedulerJob.getStatus());
				schedulerJobService.updateInfo(job);
			}
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription(" updateScheduler error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.POST,path="/v1/sys/schdljobmng/bathenable", produces={"application/json"})
	public Response bathEnable(
			@RequestParam("ids") List<String> ids,
			@RequestParam("status") int status ){
		     Response response = new Response();
			for(String id:ids){
				SchedulerJob sch=schedulerJobService.getSchedulerJobByName(id);
				if(sch!=null){
					try {
						if(status==1){
							//批量启用
							sch.setStatus(true);
							schedulerJobService.start(sch);
						}
						else{
							//批量关闭
							sch.setStatus(false);
							schedulerJobService.stop(sch);
						}
					} catch (Exception e) {
						sch.setStatus(false);
						response.setResultCode(StatusCode.UI.UI_1);
						response.setDescription("bathEnable  is error");
					    logger.error(e.getMessage(),e);
					}
					schedulerJobService.updateInfo(sch);
				}
			}
		return response;
	}
}
