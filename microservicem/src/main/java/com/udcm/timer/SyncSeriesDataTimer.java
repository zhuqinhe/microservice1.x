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
import com.udcm.model.ContentRecommendation;
import com.udcm.model.ContentScore;
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
public class SyncSeriesDataTimer implements Job{
	private static Logger log = Logger.getLogger(SyncSeriesDataTimer.class);
	@Autowired
    private MongoTemplate mongoTemplate;
	
	private void processSeries(String url){
		//db  里面获取当前定时执行到那个时间点
		Criteria criteria = new Criteria();
		criteria.and("jobName").is("SyncSeriesDataTimer");
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
				tmpurl=url+"/sync/rest/v1/cms/service/udc?starttime="+URLEncoder.encode(startTime,"UTF-8")+"&endtime="+URLEncoder.encode(endTime,"UTF-8")+"&begin="+begin+"&pagesize=20000&mediatype=2";
				String jsonstr=HttpUtils.doJsonRequest(Method.GET,tmpurl,null,3*60*1000);
				log.info("from data  is :"+jsonstr);
				Map map=JsonUtils.json2Obj(jsonstr, Map.class);
				if(map==null){
					log.info("SyncSeriesDataTimer data is null ");
					break ;
				}
				if(map!=null){
					List<Map> data=(List<Map>) map.get("data");
					MediaData vir=null;
					List<MediaData>adds=new ArrayList<MediaData>();
					List<ContentScore>cs=new ArrayList<ContentScore>();
					List<ContentRecommendation>cc=new ArrayList<ContentRecommendation>();
					if(data==null||data.isEmpty()){
						log.info("SyncSeriesDataTimer data is null ");
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
								ContentScore s=new ContentScore();
								s.setContentId(tmp.get("contentId")+"");
								s.setTotalNum(0);
								s.setTotalScore(0);
								s.setCreateTime(endTime);
								s.setUpdateTime(endTime);
								s.setName(tmp.get("name")+"");
								
								
								ContentRecommendation cct=new ContentRecommendation();
								cct.setContentId(tmp.get("contentId")+"");
								cct.setCreateTime(endTime);
								cct.setUpdateTime(endTime);
								cct.setName(tmp.get("name")+"");
								cct.setTotalA(0);
								cct.setTotalV(0);
								
								cs.add(s);
								adds.add(vir);
								cc.add(cct);
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
							mongoTemplate.insert(cs, Constants.CONTENTSCORE_COL_NAME);
							mongoTemplate.insert(cc, Constants.CONTENTRECOMMENDATION_COL_NAME);
						}
						begin=begin+20000;
						if(data.size()<20000){
							break ;
						}
				}
			} catch (Exception e) {
				log.info("SyncSeriesDataTimer is error");
				log.info(e.getMessage(),e);
				return ;
			}
		}
		criteria = new Criteria();
		criteria.and("name").is("SyncSeriesDataTimer");
		query = new Query(criteria);
		Update update=new Update();
		update.set("name","SyncSeriesDataTimer");
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
		log.info("SyncSeriesDataTimer is start,date:"+DateUtils.getCurrentTime());
		processSeries(url);
		log.info("SyncSeriesDataTimer is end,date:"+DateUtils.getCurrentTime());
	}
	
}
