/**
 * 
 */
package com.udcm.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.udcm.common.ConfigKey;
import com.udcm.common.Constants;
import com.udcm.dao.SchedulerjobDao;
import com.udcm.model.Bookmark;
import com.udcm.model.Favorite;
import com.udcm.model.NeedToDelete;
import com.udcm.model.SchedulerJob;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.DateUtils;
import com.udcm.utils.StringUtils;
import com.udcm.utils.ThreadPoolUtil;



/**
 * @Description
 * @author hoob
 * @date 2018年11月28日上午11:25:15
 */
@Component
@DisallowConcurrentExecution
public class DeleteDataTimer implements Job {
	private static Logger log = Logger.getLogger(DeleteDataTimer.class);
	@Autowired
	private MongoTemplate mongoTemplate;
	//@Resource
	//private KafkaService kafkaService;
	@Resource
	private SchedulerjobDao schedulerjobDao;

	private void processDeleteData() {
		
		Criteria criteria1 = new Criteria();
		criteria1.and("jobName").is("DeleteDataTimer");
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
		//criteria.and("updateTime").lt(time);
		criteria.and("updateTime").gt(startTime).lt(time);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.ASC, "updateTime")));
		query.limit(100);
		List<NeedToDelete> list = mongoTemplate.find(query, NeedToDelete.class, Constants.NEEDTODELETE_COL_NAME);
		while (true) {
			try {
				//List<String> delUserIdList = new ArrayList<>();
				for (NeedToDelete needToDelete : list) {
				//	delUserIdList.add(needToDelete.getUserId());

					ThreadPoolUtil.deleteTask(new Runnable() {
						@Override
						public void run() {
							// 是否需要同步
							Boolean synchronize = needToDelete.getSynchronize();
							if (null == synchronize) {
								synchronize = false;
							}
							//处理用户的收藏数据
							deteteFavorite(needToDelete.getUserId(), needToDelete.getGroupType(), synchronize);
							//处理书签
							deleteDataBookMark(needToDelete.getUserId(), needToDelete.getGroupType(), synchronize);
						}
					});
				}
			//	Criteria criteria2 = new Criteria();
			//  criteria2.and("userId").in(delUserIdList);
			//	mongoTemplate.remove(new Query(criteria2), NeedToDelete.class, Constants.NEEDTODELETE_COL_NAME);
				list = mongoTemplate.find(query, NeedToDelete.class, Constants.NEEDTODELETE_COL_NAME);
				
				if (list.size() == 0) {
					break;
				}
			} catch (Exception e) {
				log.info("DeleteDataTimer is error");
				log.info(e.getMessage(), e);
			}
		}
		job.setExecutTime(time);
		schedulerjobDao.updateInfo(job);

	}

	private void deteteFavorite(String userId,String group, Boolean synchronize){
		try{	
			//vod
			List<Favorite>favorites= deleteFavorite( userId,"2");
			//是否需要同步
			if(synchronize && "mango".equals(ConfigUtil.getProperties(ConfigKey.SCENE.name()))){
				if(favorites==null||favorites.isEmpty()){
					return ;
				}
				for (Favorite favorite : favorites) {
					//kafkaService.sendFavoriteMessage(favorite,Constants.ACTION_DELETE);
				}
			}

			//专题（不需要同步）
			favorites= deleteFavorite( userId,"32");
			//直播
			favorites= deleteFavorite( userId,"3");
			//是否需要同步
			if(synchronize && "mango".equals(ConfigUtil.getProperties(ConfigKey.SCENE.name()))){
				if(favorites==null||favorites.isEmpty()){
					return ;
				}
				for (Favorite favorite : favorites) {
					//kafkaService.sendFavoriteMessage(favorite,Constants.ACTION_DELETE);
				}
			}
			
		} catch (Exception e) {
			log.error("Send msg is Error, userId is: " + userId);
			log.error(e.getMessage(), e);
		}
	} 

	@SuppressWarnings("deprecation")
	private List<Favorite>deleteFavorite(String userId,String mediaType){
		Integer intSize = StringUtils.obj2Int(ConfigUtil.getProperties(ConfigKey.FAVORITESIZE.name()),30);
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		if(StringUtils.isNotEmpty(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<Favorite> favorites = mongoTemplate.find(query,Favorite.class,Constants.FAVORITE_COL_NAME);
		List<Favorite> deleteList = new ArrayList<Favorite>();
		if(favorites!=null&&favorites.size()>intSize){
			List<String> contentIds = new ArrayList<String>();
			for(int i=intSize;i<favorites.size();i++){
				contentIds.add(favorites.get(i).getContentId());
				deleteList.add(favorites.get(i));
				
				// 分批删除，防止数量过多，删除超时
				if (contentIds.size() > 20) {
					criteria = new Criteria();
					criteria.and("userId").is(userId);
					criteria.and("contentId").in(contentIds);
					if(StringUtils.isNotEmpty(mediaType)){
						criteria.and("mediaType").is(mediaType);
					}
					query = new Query(criteria);
					mongoTemplate.remove(query, Constants.FAVORITE_COL_NAME);
					
					contentIds.clear();
				}
				
			}
			criteria = new Criteria();
			criteria.and("userId").is(userId);
			criteria.and("contentId").in(contentIds);
			if(StringUtils.isNotEmpty(mediaType)){
				criteria.and("mediaType").is(mediaType);
			}
			query = new Query(criteria);
			mongoTemplate.remove(query, Constants.FAVORITE_COL_NAME);
		}
		return deleteList;
	}
	private void deleteDataBookMark(String userId,String group, Boolean synchronize){
		try{	
			List<Bookmark> sendList = deleteDataBookMark(userId);
			//是否需要同步
			if(synchronize && "mango".equals(ConfigUtil.getProperties(ConfigKey.SCENE.name()))){
				if(sendList==null||sendList.isEmpty()){
					return ;
				}
				for (Bookmark bookmark : sendList) {
					//kafkaService.sendBookmarkMessage(bookmark, Constants.ACTION_DELETE);
				}
			}
		} catch (Exception e) {
			log.error("Send msg is Error, userId is: " + userId);
			log.error(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public List<Bookmark> deleteDataBookMark(String userId) {

		try {
			Integer bookmarksize = StringUtils.obj2Int(ConfigUtil.getProperties(ConfigKey.BOOKMARKSIZE.name()),30);
			//查询条件
			Criteria criteria = new Criteria();
			criteria.and("userId").is(userId);
			//查出该用户所有的书签
			Query query = new Query(criteria);
			query.with(new Sort(new Order(Direction.DESC,"updateTime")));
			List<Bookmark> totalList = mongoTemplate.find(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
			//不需要删除的合集,则对应合集下的分集不做数量控制，等合集过期时一起清理   只保留对应的配置数量默认30个
			Map<String,String> bkMap = new HashMap<>();
			//需要删除的
			List<String>needdeteContentiIDs=new ArrayList<>();
			List<Bookmark>needdeteBookmarks=new ArrayList<Bookmark>();
			for (Bookmark bookmark : totalList) {
				if (bkMap.containsKey(bookmark.getContentId())) {
					//含有该合集contentId,则不需要删除
					continue;
				}else{
					if(bkMap.size()<bookmarksize){
						bkMap.put(bookmark.getContentId(), bookmark.getContentId());
					}else{
						//不是最新的合集 的记录，且超过了数量限制都删除掉
						needdeteContentiIDs.add(bookmark.getContentId());
						needdeteBookmarks.add(bookmark);
					}
					
					// 分批删除，防止数量过多，删除超时
					if (needdeteContentiIDs.size() > 20) {
						Criteria criteria1 = new Criteria();
						criteria1.and("userId").is(userId);
						criteria1.and("contentId").in(needdeteContentiIDs);
						Query query1 = new Query(criteria1);
						mongoTemplate.remove(query1, Constants.BOOKMARK_COL_NAME);
						
						needdeteContentiIDs.clear();
					}
					
				}
			}
			//删除 needdeteContentiIDs 中的数据
			if (null != needdeteContentiIDs && needdeteContentiIDs.size() != 0) {
				
				Criteria criteria1 = new Criteria();
				criteria1.and("userId").is(userId);
				criteria1.and("contentId").in(needdeteContentiIDs);
				Query query1 = new Query(criteria1);
				mongoTemplate.remove(query1, Constants.BOOKMARK_COL_NAME);
			}
			
			return needdeteBookmarks;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 获取epg地址
		if (!"1".equals(ConfigUtil.getProperties(ConfigKey.TIMERDELETE.name()))) {
			return;
		}
		log.info("DeleteDataTimer is start,date:" + DateUtils.getCurrentTime());
		processDeleteData();
		log.info("DeleteDataTimer is end,date:" + DateUtils.getCurrentTime());
	}

}
