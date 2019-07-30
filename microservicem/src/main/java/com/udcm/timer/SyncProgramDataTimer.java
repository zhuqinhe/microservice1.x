/**
 * 
 */
package com.udcm.timer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.udcm.common.ConfigKey;
import com.udcm.common.Constants;
import com.udcm.common.Method;
import com.udcm.model.MediaData;
import com.udcm.model.SchedulerJob;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.DateUtils;
import com.udcm.utils.HttpUtils;
import com.udcm.utils.JsonUtils;
import com.udcm.utils.StringUtils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月28日上午11:25:15
 */
@Component
@DisallowConcurrentExecution
public class SyncProgramDataTimer implements Job{
	private static Logger log = Logger.getLogger(SyncProgramDataTimer.class);
	@Autowired
    private MongoTemplate mongoTemplate;
	
	/**
	 * @Title execute
	 * @Description 
	 * @param 
	 * @return Job
	 * @throws 
	 */
	/*@Scheduled(fixedRate = 1000*60*10)
	public void execute(){
		//获取epg地址
		String url=ConfigUtil.getProperties(ConfigKey.EPGSERVERURL.name());
		if(StringUtils.isEmpty(url)){
			return ;
		}
		log.info("SyncVirtualIdProgramTimer is start,date:"+DateUtils.getCurrentTime());
		processSeries(url);
		log.info("SyncVirtualIdProgramTimer is end,date:"+DateUtils.getCurrentTime());
		
	}*/
	private void processProgram(String url){

		Criteria criteria = new Criteria();
		criteria.and("jobName").is("SyncProgramDataTimer");
		Query query = new Query(criteria);
		SchedulerJob job=mongoTemplate.findOne(query, SchedulerJob.class,Constants.SCHEDULERJOB_COL_NAME);
		String startTime="";
		if(StringUtils.isNotEmpty(job.getExecutTime())){
			startTime=job.getExecutTime();
		}else{
			startTime="1970-01-01 00:00:00";
		}
		String tmpurl=url;
		Integer begin=0;
		String endTime=DateUtils.getCurrentTime();
		while(true){
			try {
				tmpurl=url+"/sync/rest/v1/cms/service/udc?starttime="+URLEncoder.encode(startTime,"UTF-8")+"&endtime="+URLEncoder.encode(endTime,"UTF-8")+"&begin="+begin+"&pagesize=20000&mediatype=3";
				String jsonstr=HttpUtils.doJsonRequest(Method.GET,tmpurl,null,3*60*1000);
				log.info("from data  is :"+jsonstr);
				Map map=JsonUtils.json2Obj(jsonstr, Map.class);
				if(map==null){
					log.info("SyncProgramDataTimer data is null ");
					break ;
				}
				if(map!=null){
					List<Map> data=(List<Map>) map.get("data");
					MediaData vir=null;
					List<MediaData>adds=new ArrayList<MediaData>();
					if(data==null||data.isEmpty()){
						log.info("SyncProgramDataTimer data is null ");
						break ;
					}
				
					for(Map tmp:data){
							//先检查db中是否含有，有的不做处理
							if(tmp==null||tmp.isEmpty()||tmp.get("contentId")==null){
								continue ;
							}
							criteria = new Criteria();
							criteria.and("contentId").is(tmp.get("contentId"));
							query = new Query(criteria);
							vir=mongoTemplate.findOne(query, MediaData.class,Constants.MEDIADATA_COL_NAME);
							if(vir==null){
								//不存在  构建一个
								vir=new MediaData();
								vir.setMediaType("2");
								vir.setContentId(tmp.get("contentId").toString());
							
								if(tmp.get("detailPicUrl")!=null){
									vir.setThumbnailUrl(tmp.get("detailPicUrl").toString());
								}
								if(tmp.get("seriesFlag")==null){
									vir.setSeriesFlag("1");
								}else{
									vir.setSeriesFlag(tmp.get("seriesFlag").toString());
								}
								if(tmp.get("vodType")!=null){
									vir.setProgramType(tmp.get("vodType").toString());
								}
								if(tmp.get("totalNumber")!=null){
									vir.setTotalNumber(tmp.get("totalNumber").toString());
								}
								if(tmp.get("nowNumber")!=null){
									vir.setNowNumber(tmp.get("nowNumber").toString());
								}
								adds.add(vir);
							}else{
								if(tmp.get("detailPicUrl")!=null){
									vir.setThumbnailUrl(tmp.get("detailPicUrl").toString());
								}else{
									vir.setThumbnailUrl("");
								}
								Criteria ctmp = new Criteria();
								ctmp.and("contentId").is(vir.getContentId());
								Query cquery = new Query(ctmp);
								Update update=new Update();
								update.set("thumbnailUrl",vir.getThumbnailUrl());
								update.set("totalNumber",vir.getTotalNumber());
								update.set("nowNumber",vir.getNowNumber());
								mongoTemplate.upsert(cquery,update,MediaData.class);
							}
						}
						if(adds.size()>0){
							mongoTemplate.insert(adds, Constants.MEDIADATA_COL_NAME);
						}
						begin=begin+20000;
						if(data.size()<20000){
							break ;
						}
				}
			} catch (Exception e) {
				log.info("SyncProgramDataTimer is error");
				log.info(e.getMessage(),e);
				return ;
			}
		}
		criteria = new Criteria();
		criteria.and("jobName").is("SyncProgramDataTimer");
		query = new Query(criteria);
		Update update=new Update();
		update.set("jobName","SyncProgramDataTimer");
		update.set("executTime",endTime);
		mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), SchedulerJob.class,Constants.SCHEDULERJOB_COL_NAME);
	}
	/**
	 * @Title executeInternal
	 * @Description 
	 * @param 
	 * @return QuartzJobBean
	 * @throws 
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
	    //获取epg地址
		String url=ConfigUtil.getProperties(ConfigKey.EPGSERVERURL.name());
		if(StringUtils.isEmpty(url)){
			return ;
		}
		log.info("SyncProgramDataTimer is start,date:"+DateUtils.getCurrentTime());
		processProgram(url);
		log.info("SyncProgramDataTimer is end,date:"+DateUtils.getCurrentTime());
		
	}
	
}
