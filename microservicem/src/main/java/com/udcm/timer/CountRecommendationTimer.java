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
import com.udcm.model.ContentRecommendation;
import com.udcm.model.Recommendation;
import com.udcm.model.SchedulerJob;
import com.udcm.utils.DateUtils;
import com.udcm.utils.StringUtils;


/**
 * 统计踩/赞定时器
 * <p>Title: CountRecommendationTimer</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月11日
 */
public class CountRecommendationTimer implements Job{
	private static Logger log = Logger.getLogger(CountRecommendationTimer.class);
	@Autowired
    private MongoTemplate mongoTemplate;
	@Resource
	private SchedulerjobDao schedulerjobDao;
	@SuppressWarnings("rawtypes")
	private void countRecommendation() {
		
		Criteria criteria1 = new Criteria();
		criteria1.and("jobName").is("CountRecommendationTimer");
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
		List<Recommendation> list = mongoTemplate.find(query, Recommendation.class, Constants.RECOMMENDATION_COL_NAME);
		
		while(true){
			 if(list!=null&&list.size()>0){
            	 for(Recommendation s:list){
            		//循环处理需要处理的数据
            		    Criteria criteria2 = new Criteria();
            			criteria2.and("contentId").is(s.getContentId());
            			Query query2 = new Query(criteria2);
            			Update update=new Update();
            			update.set("updateTime",time);
            			if("a".equalsIgnoreCase(s.getChoice())){
            				if(s.getRecycle()==1){
                    			update.inc("totalA",-1);
                    			mongoTemplate.remove(s,Constants.RECOMMENDATION_COL_NAME);
            				}else{
            					update.inc("totalA",1);
            				}
            			}else if("v".equalsIgnoreCase(s.getChoice())){
            				if(s.getRecycle()==1){
                    			update.inc("totalV",-1);
                    			mongoTemplate.remove(s,Constants.RECOMMENDATION_COL_NAME);
            				}else{
            					update.inc("totalV",1);
            				}
            			}
            		    mongoTemplate.findAndModify(query2, update, new FindAndModifyOptions().returnNew(true).upsert(true),ContentRecommendation.class);
            	 }
            	 list = mongoTemplate.find(query, Recommendation.class, Constants.RECOMMENDATION_COL_NAME);
            	 if(list.size()==0){
            		 break;
            	 }
             }

		}
		
		//处理完后更新定时器执行时间
		job.setExecutTime(time);
		schedulerjobDao.updateInfo(job);

		
		
		/*

		//允许使用磁盘缓存
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
		// 踩/赞记录聚合
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.group("$contentId","$choice").count().as("num")
				).withOptions(options);

		AggregationResults<HashMap> output = 
				mongoTemplate.aggregate(aggregation, Constants.MONGO_RECOMMENDATION_COL_NAME, HashMap.class);
		// 聚合记录处理
		if (!output.getMappedResults().isEmpty()) {
			List<ContentRecommendation> list = new ArrayList<>();
			for (HashMap hashMap : output) {
				list.add(new ContentRecommendation((String) hashMap.get("contentId"),
						hashMap.get("choice").equals("A") ? (int) hashMap.get("num") : 0,
						hashMap.get("choice").equals("V") ? (int) hashMap.get("num") : 0,
						DateUtils.getCurrentTime(),
						DateUtils.getCurrentTime()));
			}

			// 合并contentId相同的踩/赞总数记录
			ContentRecommendation tmpCR = null;
			Map<String, ContentRecommendation> map = new HashMap<String, ContentRecommendation>();
			for (ContentRecommendation contentRecommendation : list) {
				tmpCR = map.get(contentRecommendation.getContentId());
				if (tmpCR != null) {
					tmpCR.setTotalA(tmpCR.getTotalA() + contentRecommendation.getTotalA());
					tmpCR.setTotalV(tmpCR.getTotalV() + contentRecommendation.getTotalV());
				} else {
					map.put(contentRecommendation.getContentId(), contentRecommendation);
				}
			}

			List<ContentRecommendation> resList = new ArrayList<>();
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				resList.add(map.get(it.next()));
			}

			// 批量修改记录
			BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED,
					Constants.MONGO_CONTENTRECOMMENDATION_COL_NAME);
			
			String countTime = DateUtils.getCurrentTime();
			for (ContentRecommendation contentRecommendation : resList) {
				Update update = new Update();
				update.set("totalA", contentRecommendation.getTotalA());
				update.set("totalV", contentRecommendation.getTotalV());
				update.set("createTime", contentRecommendation.getCreateTime());
				update.set("updateTime", DateUtils.getCurrentTime());
				ops.upsert(new Query(new Criteria().and("contentId").is(contentRecommendation.getContentId())), update);
			}
			ops.execute();
			
			//删除未更新（recommendation_collection中存在，contentrecommendation_collection中不存在的数据）的统计记录
			Criteria criteria = new Criteria();
			criteria.and("updateTime").lt(countTime);
			mongoTemplate.remove(new Query(criteria), ContentRecommendation.class, Constants.MONGO_CONTENTRECOMMENDATION_COL_NAME);
		}else {
			//聚合记录为空时，删除所有统计记录
			mongoTemplate.remove(new Query(), ContentRecommendation.class, Constants.MONGO_CONTENTRECOMMENDATION_COL_NAME);
		}
	*/}
	
	/**  
	 * <p>Title: executeInternal</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月11日   
	 * @param arg0
	 * @throws JobExecutionException  
	 */ 
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("CountRecommedation is start,date:"+DateUtils.getCurrentTime());
		countRecommendation();
		log.info("CountRecommedation is end,date:"+DateUtils.getCurrentTime());
	}

}
