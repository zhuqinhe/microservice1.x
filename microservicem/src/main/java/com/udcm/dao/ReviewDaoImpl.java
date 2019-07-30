/**
 * 
 */
package com.udcm.dao;


import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.udcm.common.Constants;
import com.udcm.model.Comment;
import com.udcm.model.ContentRecommendation;
import com.udcm.model.ContentScore;
import com.udcm.model.Recommendation;
import com.udcm.model.Reminder;
import com.udcm.model.Score;
import com.udcm.utils.StringUtils;




/**
 * 
 * <p>Title: ReviewDaoImpl</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月2日
 */
@Repository("reviewDao")
public class ReviewDaoImpl implements ReviewDao{
	@Resource
    private MongoTemplate mongoTemplate;

	/**  
	 * <p>Title: getComment</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param contentId
	 * @param commentId
	 * @param begin
	 * @param pagesize
	 * @return  
	 */ 
	@Override
	public List<Comment> getComment(String contentId, String userId, Integer begin, Integer pagesize) {
		
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		
		return mongoTemplate.find(new Query(criteria).skip(begin).limit(pagesize), Comment.class, Constants.COMMENT_COL_NAME);
	}
	
	/**
	 * 递归查询子评论
	 * <p>Title: getChildComment</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param list	参数
	 * @param resList 结果集
	 * @return
	 */
	public List<Comment> getChildComment(List<Comment> list,List<Comment> resList) {
		for (Comment comment : list) {
			Criteria criteria = new Criteria();
			criteria.and("commentParentId").is(comment.getId());
			List<Comment> childList = mongoTemplate.find(new Query(criteria), Comment.class, Constants.COMMENT_COL_NAME);
			
			if (!StringUtils.objIsNull(childList)) {	
				resList.addAll(childList);
				getChildComment(childList, resList);
			}
		}
		
		return resList;
	}

	/**
	 * @Title getContentScore
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public List<ContentScore> getContentScore(String contentId,String name,int begin, int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		if(StringUtils.isNotEmpty(name)){
			Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("name").regex(pattern);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return mongoTemplate.find(query,ContentScore.class,Constants.CONTENTSCORE_COL_NAME);
		
	}

	/**
	 * @Title getContentScoreCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getContentScoreCount(String contentId,String name) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		if(StringUtils.isNotEmpty(name)){
			Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("name").regex(pattern);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.CONTENTSCORE_COL_NAME);
	}

	/**
	 * @Title getRecommendation
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public List<ContentRecommendation> getRecommendation(String contentId,String name,int begin, int pageSize) {
		Criteria criteria = new Criteria();
		
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		if(StringUtils.isNotEmpty(name)){
			Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("name").regex(pattern);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return mongoTemplate.find(query,ContentRecommendation.class,Constants.CONTENTRECOMMENDATION_COL_NAME);
	}

	/**
	 * @Title getRecommendationCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getRecommendationCount(String contentId,String name) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		if(StringUtils.isNotEmpty(name)){
			Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("name").regex(pattern);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.CONTENTRECOMMENDATION_COL_NAME);
	}


	/**
	 * @Title getCommentCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getCommentCount(String contentId,String userId) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		if(StringUtils.isNotEmpty(contentId)){
			criteria.and("contentId").is(contentId);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.COMMENT_COL_NAME);
	}

	/**
	 * @Title deleteComment
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public void deleteComment(List<String> ids) {
		Criteria criteria = new Criteria();
		criteria.and("_id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Constants.COMMENT_COL_NAME);
		
	}

	/**
	 * @Title getReminder
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public List<Reminder> getReminder(String userId, int begin, int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return  mongoTemplate.find(query,Reminder.class,Constants.REMINDER_COL_NAME);
	}

	/**
	 * @Title getReminderCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getReminderCount(String userId) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.REMINDER_COL_NAME);
	}

	/**
	 * @Title deleteReminder
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public void deleteReminder(List<String> ids) {
		Criteria criteria = new Criteria();
		criteria.and("_id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Constants.REMINDER_COL_NAME);
		
		
	}

	/**
	 * @Title getScore
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public List<Score> getScore(String userId, int begin, int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		query.skip(begin).limit(pageSize);
		return  mongoTemplate.find(query,Score.class,Constants.SCORE_COL_NAME);
		
	}

	/**
	 * @Title getScoreCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getScoreCount(String userId) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.SCORE_COL_NAME);
	}

	/**
	 * @Title getRecommendations
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public List<Recommendation> getRecommendations(String userId, int begin,
			int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		query.skip(begin).limit(pageSize);
		return  mongoTemplate.find(query,Recommendation.class,Constants.RECOMMENDATION_COL_NAME);
	}

	/**
	 * @Title getRecommendationCount
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	@Override
	public int getRecommendationCount(String userId) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.RECOMMENDATION_COL_NAME);
	}
	
}
