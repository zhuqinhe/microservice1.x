/**
 * 
 */
package com.udcm.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.udcm.common.ConfigKey;
import com.udcm.common.Constants;
import com.udcm.model.Bookmark;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.StringUtils;
/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:33:38
 */
@Repository("bookmarkDao")
public class BookmarkDaoImpl implements BookmarkDao{
	@Resource
    private MongoTemplate mongoTemplate;
   
	
	@Override
	public List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize) {
		
		//查询条件
		Criteria criteria = new Criteria();
		if (StringUtils.isNotEmpty(userId)) {
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return mongoTemplate.find(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}
	@Override
	public Integer getCounts(String userId) {
		
		//查询条件
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		
		return (int) mongoTemplate.count(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}

	
	@Override
	public List<Bookmark> removeBookmark(String userId, Object[] pcArr) {

		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);

		if (!(pcArr.length == 1 && "_all_".equals(pcArr[0]))) {
			criteria.and("contentId").in(Arrays.asList(pcArr));
		}
		Query query = new Query(criteria);

		return mongoTemplate.findAllAndRemove(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}
	

	@Override
	public List<Bookmark> removeBookmark(String userId, String contentId, Integer index) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").is(contentId);
		criteria.and("index").is(index);
		Query query = new Query(criteria);
	    return 	mongoTemplate.findAllAndRemove(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}
	

	/**
	 * @Title deleteBookmarkByBookmarkSize
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<Bookmark> deleteBookmarkByBookmarkSize(String userId, String size,String mediaType) {
		Integer bookmarksize = StringUtils.obj2Int(ConfigUtil.getProperties(ConfigKey.BOOKMARKSIZE.name()),30);
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		//查出该用户所有的书签
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<Bookmark> totalList = mongoTemplate.find(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
		/* jdk1.8新特性 lambda表达式分组  */
		//Map<String,List<Bookmark>> bkMap = totalList.stream().collect(Collectors.groupingBy(Bookmark::getContentId));
		//分组
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
					//不是最新的合集 的记录，且超过了数量xian都删出点
					needdeteContentiIDs.add(bookmark.getContentId());
					needdeteBookmarks.add(bookmark);
				}

			}
		}
		//遍历分组取 min(updateTime)
		Criteria criteria1 = new Criteria();
		criteria1.and("userId").is(userId);
		criteria1.and("mediaType").is(mediaType);
		criteria1.and("contentId").in(needdeteContentiIDs);
		Query query1 = new Query(criteria1);
		mongoTemplate.remove(query1, Constants.BOOKMARK_COL_NAME);
		return needdeteBookmarks;
	}

	
	@Override
	public List<Bookmark> getBookmarks(String userId, String stbid) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Bookmark> getBookmarkListByGroup(String userId, Integer begin, Integer pageSize, String sp, String contentId, String date) {
		
		 //查询条件
		  Criteria criteria = new Criteria();
		  criteria.and("userId").is(userId);
		  
		  if (StringUtils.isNotEmpty(contentId)) {
			 criteria.and("contentId").is(contentId);
		  }
		  
		  //查出该用户所有的书签
		  Query query = new Query(criteria);
		  query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		  query.limit(1000);
		  //时间倒叙只查其中的1000条（数量可配置）
		  List<Bookmark> totalList = mongoTemplate.find(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
		  /* jdk1.8新特性 lambda表达式分组  */
		  //Map<String,List<Bookmark>> bkMap = totalList.stream().collect(Collectors.groupingBy(Bookmark::getContentId));
		  //分组
		  Map<String,String> bkMap = new HashMap<>();//存储每个合集下的最新的一个分集
		  List<Bookmark> resList = new LinkedList<>();
		  
		   //默认只要50个
		  Integer size = StringUtils.obj2Int(ConfigUtil.getProperties(ConfigKey.BOOKMARKSIZE.name()),30);
		  for (Bookmark bookmark : totalList) {
			  if (bkMap.containsKey(bookmark.getContentId())) {
				  //是否含有该合集，有就不处理，直接跳过  已经保存了最新的一个
				  continue;
			  }else{
				  bkMap.put(bookmark.getContentId(),bookmark.getContentId());
				  resList.add(bookmark);
			  }
			  if(resList.size()>=size){
				  break ;
			  }
		  }
		  return resList;
	}

	/**  
	 * 删除 saveList 之外的所有记录
	 * <p>Title: removeAllNotInclude</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年2月13日   
	 * @param userId
	 * @param saveList  
	 */ 
	@Override
	public void removeAllNotInclude(String userId, List<String> saveList) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").nin(saveList);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		Criteria criteria = new Criteria();
		criteria.and("id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Bookmark.class, Constants.BOOKMARK_COL_NAME);
		
	}
	/**
	 * @Title getUserIds
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@Override
	public List<String> getUserIds(Integer begin, Integer pageSize) {
		//BasicDBObject query = new BasicDBObject();

		//mongoTemplate.getCollection(Constants.MONGO_BOOKMARK_COL_NAME).distinct("userId", query);;
		//	Criteria criteria = new Criteria();  
		//	List<Order> orders = new ArrayList<Order>();  
		//orders.add(new Order(Direction.ASC, "_id"));  
		//Sort sort = new Sort(orders);  
		Aggregation agg = Aggregation.newAggregation(  
				//    Aggregation.match(criteria),  //查询条件
				Aggregation.group("userId"),
				//   Aggregation.sort(sort),
				Aggregation.skip(begin),//跳到第几个开始
				Aggregation.limit(pageSize)//查出多少个数据
				);
		List<String> details = new ArrayList<String>();
		AggregationResults<BasicDBObject> outputType=mongoTemplate.aggregate(agg,Constants.BOOKMARK_COL_NAME, BasicDBObject.class);  
		for (Iterator<BasicDBObject> iterator = outputType.iterator(); iterator.hasNext();) {  
			DBObject obj =iterator.next(); 
			details.add((String) obj.get("_id"));
		}
		return details;
		
		
	}
	/**
	 * @Title getUserIdCount
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@Override
	public int getUserIdCount() {
		Aggregation agg = Aggregation.newAggregation(  
				//    Aggregation.match(criteria),  //查询条件
				Aggregation.group("userId").count().as("total")
				//   Aggregation.sort(sort),
				);  
		
		AggregationResults<BasicDBObject> outputType=mongoTemplate.aggregate(agg,Constants.BOOKMARK_COL_NAME, BasicDBObject.class);  
		for (Iterator<BasicDBObject> iterator = outputType.iterator(); iterator.hasNext();) {  
			DBObject obj =iterator.next(); 
			return (int) obj.get("total");
		}
		return 0;
	}
}
