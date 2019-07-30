/**
 * 
 */
package com.udcm.timer;


import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.udcm.common.Constants;
import com.udcm.dao.SchedulerjobDao;
import com.udcm.model.ContentScore;
import com.udcm.model.SchedulerJob;
import com.udcm.model.Score;
import com.udcm.utils.DateUtils;
import com.udcm.utils.StringUtils;

/**
 * 计算平均分定时器
 * <p>Title: CaculateScoreTimer</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月9日
 */
public class CaculateScoreTimer implements Job{
	private static Logger log = Logger.getLogger(CaculateScoreTimer.class);
	@Autowired
    private MongoTemplate mongoTemplate;
	@Resource
	private SchedulerjobDao schedulerjobDao;
	
	private void caculateScore(){

		//计算平均分  后面做异常处理，不因一个异常导整次计算失败
		Criteria criteria1 = new Criteria();
		criteria1.and("jobName").is("CaculateScoreTimer");
		Query query1 = new Query(criteria1);
		SchedulerJob job=mongoTemplate.findOne(query1, SchedulerJob.class,Constants.SCHEDULERJOB_COL_NAME);
		String startTime="";
		if(StringUtils.isNotEmpty(job.getExecutTime())){
			startTime=job.getExecutTime();
		}else{
			startTime="1970-01-01 00:00:00";
		}
		String time = DateUtils.getCurrentTime();
		// 获取需要删除的用户数据
		Criteria criteria = new Criteria();
		criteria.and("updateTime").gt(startTime).lt(time);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.ASC, "updateTime")));
		query.limit(200);
		List<Score> list = mongoTemplate.find(query, Score.class, Constants.SCORE_COL_NAME);
		while(true){
             if(list!=null&&list.size()>0){
            	 for(Score s:list){
            		//循环处理需要处理的数据
            		    Criteria criteria2 = new Criteria();
            			criteria2.and("contentId").is(s.getContentId());
            			Query query2 = new Query(criteria2);
            			Update update=new Update();
            			update.set("updateTime",time);
            			update.inc("totalScore",s.getScore());
            			update.inc("totalNum",1);
            		    mongoTemplate.findAndModify(query2, update, new FindAndModifyOptions().returnNew(true).upsert(true),ContentScore.class);
            	 }
            	 list = mongoTemplate.find(query, Score.class, Constants.SCORE_COL_NAME);
            	 if(list.size()==0){
            		 break;
            	 }
             }
		}
		//处理完后更新定时器执行时间
		job.setExecutTime(time);
		schedulerjobDao.updateInfo(job);


		/*//查询原有记录
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<ContentScore> listOld = mongoTemplate.find(query, ContentScore.class, Constants.MONGO_CONTENTSCORE_COL_NAME);
		//最后更新的一条记录
		String updateTime = "2000-01-01 00:00:00";
		if (!listOld.isEmpty()) {
			updateTime = listOld.get(0).getUpdateTime();
		}

		//允许使用磁盘缓存
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();

		//增量聚合
		Aggregation aggregation = Aggregation.newAggregation(
				//skip数据量大的时候速度慢
				//Aggregation.skip(totalCount),
				//利用时间更新时间来判断
				Aggregation.match(new Criteria().and("updateTime").gt(updateTime)),
                Aggregation.group("$contentId").count().as("totalNum").sum("$score").as("totalScore"))
				.withOptions(options);
		AggregationResults<HashMap> output =
		        mongoTemplate.aggregate(aggregation, Constants.MONGO_SCORE_COL_NAME, HashMap.class);

		//增量记录
		List<ContentScore> list = new ArrayList<>();
		if (!output.getMappedResults().isEmpty() && output.getMappedResults().size() > 0) {
			for (HashMap hashMap : output) {
				list.add(new ContentScore(Utils.obj2Str(hashMap.get("_id")), 
						((Number)hashMap.get("totalScore")).intValue(),
						((Number)hashMap.get("totalNum")).intValue(),
						DateUtils.getCurrentTime(),DateUtils.getCurrentTime()));
			}

		 *//**
		 * 原有记录不为空：原有记录+增量记录
		 * 原记录为空： 直接添加 
		 *//*
			List<ContentScore> resList = new ArrayList<>();

			Boolean flag = true;
			if (!listOld.isEmpty()) {
				for (ContentScore contentScore : list) {
					for (ContentScore contentScoreOld : listOld) {
						if (contentScore.getContentId().equals(contentScoreOld.getContentId())) {
							resList.add(new ContentScore(contentScoreOld.getContentId(),
											contentScoreOld.getTotalScore() + contentScore.getTotalScore(),
											contentScoreOld.getTotalNum() + contentScore.getTotalNum(),
											contentScoreOld.getCreateTime(), contentScore.getUpdateTime()));
							flag = false;
						}
					}
				}
				//当contentscore_collection中有记录，但没有与score_collection中contentId相同的记录时，直接添加（正常不会出现此种情况）
				if(flag){
					resList = list;
				}
			}else {
				resList = list;
			}

			//批量修改记录
			BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, Constants.MONGO_CONTENTSCORE_COL_NAME);
			for (ContentScore contentScore : resList) { 
				Update update = new Update();
				update.set("totalScore", contentScore.getTotalScore());
				update.set("totalNum", contentScore.getTotalNum());
				update.set("createTime", contentScore.getCreateTime());
				update.set("updateTime", DateUtils.getCurrentTime());
				ops.upsert(new Query(new Criteria().and("contentId").is(contentScore.getContentId())), update);
			}

			ops.execute();
		}*/
	}


	/**  
	 * <p>Title: executeInternal</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月9日   
	 * @param arg0
	 * @throws JobExecutionException  
	 */ 
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("caculateScore is start,date:"+DateUtils.getCurrentTime());
		caculateScore();
		log.info("caculateScore is end,date:"+DateUtils.getCurrentTime());
	}
}
