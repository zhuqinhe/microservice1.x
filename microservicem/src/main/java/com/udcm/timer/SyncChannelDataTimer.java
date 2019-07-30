/**
 * 
 */
package com.udcm.timer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
public class SyncChannelDataTimer implements Job{
	private static Logger log = Logger.getLogger(SyncChannelDataTimer.class);
	@Autowired
    private MongoTemplate mongoTemplate;

	private void processChannel(String url){
		//db  里面获取当前定时执行到那个时间点
		Criteria criteria = new Criteria();
		criteria.and("jobName").is("SyncChannelDataTimer");
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
					log.info("SyncChannelDataTimer data is null ");
					break ;
				}
				if(map!=null){
					List<Map> data=(List<Map>) map.get("data");
					MediaData vir=null;
					List<MediaData>adds=new ArrayList<MediaData>();
					if(data==null||data.isEmpty()){
						log.info("SyncChannelDataTimer data is null ");
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
								vir.setMediaType("3");
								vir.setContentId(tmp.get("contentId").toString());
								
								if(tmp.get("logoUrl")!=null){
									vir.setThumbnailUrl(tmp.get("logoUrl").toString());
								}
								adds.add(vir);
							}else{
								if(tmp.get("logoUrl")!=null){
									vir.setThumbnailUrl(tmp.get("logoUrl").toString());
								}else{
									vir.setThumbnailUrl("");
								}
								Criteria ctmp = new Criteria();
								ctmp.and("contentId").is(vir.getContentId());
								Query cquery = new Query(ctmp);
								Update update=new Update();
								update.set("thumbnailUrl",vir.getThumbnailUrl());
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
				log.info("SyncChannelDataTimer is error");
				log.info(e.getMessage(),e);
				return ;
			}
		}
		criteria = new Criteria();
		criteria.and("name").is("SyncChannelDataTimer");
		query = new Query(criteria);
		Update update=new Update();
		update.set("name","SyncChannelDataTimer");
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
		log.info("SyncChannelDataTimer is start,date:"+DateUtils.getCurrentTime());
		processChannel(url);
		log.info("SyncChannelDataTimer is end,date:"+DateUtils.getCurrentTime());
	}
	
}
