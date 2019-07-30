/**
 * 
 */
package com.bookmark.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.bookmark.constants.ConfigKey;
import com.bookmark.constants.Constants;
import com.bookmark.model.Bookmark;
import com.bookmark.utils.StringUtils;
import com.bookmark.utils.Utils;


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
	public Bookmark add(Bookmark bookmark) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(bookmark.getUserId());
		criteria.and("contentId").is(bookmark.getContentId());
		criteria.and("index").is(bookmark.getIndex());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("createTime",bookmark.getCreateTime());
		update.set("name", bookmark.getName());
		update.set("pContentId", bookmark.getpContentId());
		update.set("episodeName", bookmark.getEpisodeName());
		update.set("index", bookmark.getIndex());
		update.set("time", bookmark.getTime());
		update.set("length", bookmark.getLength());
		update.set("thumbnailUrl", bookmark.getThumbnailUrl());
		update.set("updateTime",bookmark.getUpdateTime());
		update.set("periods",bookmark.getPeriods());
		update.set("mediaType",bookmark.getMediaType());
		update.set("programType",bookmark.getProgramType());
		update.set("source", bookmark.getSource());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), Bookmark.class,Constants.MONGO_BOOKMARK_COL_NAME);
		
		//mongoTemplate.save(bookmark, Constants.MONGO_BOOKMARK_COL_NAME);
	}

	@Override
	public List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize, String sp, String contentId, String date) {
		
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		if (StringUtils.isNotEmpty(contentId)) {
			criteria.and("contentId").is(contentId);
		}
		
		criteria.and("updateTime").gt(date);
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return mongoTemplate.find(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}

	@Override
	public Integer getCounts(String userId) {
		
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		Query query = new Query(criteria);
		
		return (int) mongoTemplate.count(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}

	
	@Override
	public List<Bookmark> removeBookmark(String userId, String[] pcArr) {

		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);

		if (!(pcArr.length == 1 && "_all_".equals(pcArr[0]))) {
			criteria.and("contentId").in(Arrays.asList(pcArr));
		}
		Query query = new Query(criteria);

		return mongoTemplate.findAllAndRemove(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}
	

	@Override
	public List<Bookmark> removeBookmark(String userId, String contentId, Integer index) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").is(contentId);
		criteria.and("index").is(index);
		Query query = new Query(criteria);
	    return 	mongoTemplate.findAllAndRemove(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}
	

	@Override
	public List<Bookmark> checkBookmark(String userId, String contentId,Integer index) {
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").is(contentId);
		if(index!=null){
			criteria.and("index").is(index);
		}
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}

	/**
	 * @Title deleteBookmarkByBookmarkSize
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@Override
	public List<Bookmark> deleteBookmarkByBookmarkSize(String userId, String size,String mediaType) {
		Integer bookmarksize = Utils.obj2Int(null,30);
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		//查出该用户所有的书签
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<Bookmark> totalList = mongoTemplate.find(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
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
		mongoTemplate.remove(query1, Constants.MONGO_BOOKMARK_COL_NAME);
		return needdeteBookmarks;
	}

	/**
	 * @Title addOrUpdateSingleBookmark
	 * @Description 
	 * @param 
	 * @return BookmarkDao
	 * @throws 
	 */
	@Override
	public Bookmark addOrUpdateSingleBookmark(Bookmark bookmark) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(bookmark.getUserId());
		criteria.and("contentId").is(bookmark.getContentId());
		//criteria.and("index").is(bookmark.getIndex());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("createTime",bookmark.getCreateTime());
		update.set("name", bookmark.getName());
		update.set("pContentId", bookmark.getpContentId());
		update.set("episodeName", bookmark.getEpisodeName());
		update.set("index", bookmark.getIndex());
		update.set("time", bookmark.getTime());
		update.set("length", bookmark.getLength());
		update.set("thumbnailUrl", bookmark.getThumbnailUrl());
		update.set("updateTime",bookmark.getUpdateTime());
		update.set("periods",bookmark.getPeriods());
		update.set("mediaType",bookmark.getMediaType());
		update.set("programType",bookmark.getProgramType());
		update.set("source", bookmark.getSource());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), Bookmark.class,Constants.MONGO_BOOKMARK_COL_NAME);
	}

	/* (non-Javadoc)
	 * @see com.fonsview.udc.dao.BookmarkDao#getBookmarks(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Bookmark> getBookmarks(String userId, String stbid) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
	}

	/* (non-Javadoc)
	 * @see com.fonsview.udc.dao.BookmarkDao#getBookmarkListByGroup(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
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
		  List<Bookmark> totalList = mongoTemplate.find(query, Bookmark.class, Constants.MONGO_BOOKMARK_COL_NAME);
		  /* jdk1.8新特性 lambda表达式分组  */
		  //Map<String,List<Bookmark>> bkMap = totalList.stream().collect(Collectors.groupingBy(Bookmark::getContentId));
		  //分组
		  Map<String,String> bkMap = new HashMap<>();//存储每个合集下的最新的一个分集
		  List<Bookmark> resList = new LinkedList<>();
		  
		   //默认只要50个
		  Integer size = Utils.obj2Int(null,30);
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
}
