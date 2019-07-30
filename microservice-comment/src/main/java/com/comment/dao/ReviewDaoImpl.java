/**
 * 
 */
package com.comment.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.comment.constants.Constants;
import com.comment.model.Comment;
import com.comment.model.ContentRecommendation;
import com.comment.model.ContentScore;
import com.comment.model.Recommendation;
import com.comment.model.Score;
import com.comment.utils.DateUtils;
import com.comment.utils.Utils;


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
	 * <p>Title: addScore</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param score
	 * @return  
	 */ 
	@Override
	public void addScore(Score score) {
		
		mongoTemplate.insert(score, Constants.MONGO_SCORE_COL_NAME);
	}

	/**
	 * 查询用户评分
	 * <p>Title: getScore</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param userId
	 * @param contentId
	 * @return
	 */
	@Override
	public Score getScore(String userId, String contentId) {
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").is(contentId);
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query, Score.class, Constants.MONGO_SCORE_COL_NAME);
	}

	/**  
	 * 根据内容ID获取平均分 
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param contentId
	 * @return  
	 */ 
	@Override
	public Float getAvgScore(String contentId) {
		
		Criteria criteria = new Criteria();
		criteria.and("contentId").is(contentId);
		Query query = new Query(criteria);
		ContentScore contentScore = mongoTemplate.findOne(query, ContentScore.class, Constants.MONGO_CONTENTSCORE_COL_NAME);
		
		//保留一位小数
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		Float avgScore = 0f;
		if (!Utils.objIsNull(contentScore)) {
			avgScore = ((float)contentScore.getTotalScore()/contentScore.getTotalNum());
		}
		return Float.valueOf(decimalFormat.format(avgScore));
	}

	/**  
	 * <p>Title: getRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param recommendation
	 * @return  
	 */ 
	@Override
	public Recommendation getRecommendation(Recommendation recommendation) {
		//查询条件
		Criteria criteria = new Criteria();
		criteria.and("userId").is(recommendation.getUserId());
		criteria.and("contentId").is(recommendation.getContentId());
		Query query = new Query(criteria);
		
		return mongoTemplate.findOne(query, Recommendation.class, Constants.MONGO_RECOMMENDATION_COL_NAME);
	}

	/**  
	 * <p>Title: addRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param recommendation  
	 */ 
	@Override
	public void addRecommendation(Recommendation recommendation) {
		
		mongoTemplate.insert(recommendation, Constants.MONGO_RECOMMENDATION_COL_NAME);
		
	}

	/**  
	 * 删除踩/赞记录 
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param recommendation  
	 */ 
	@Override
	public void removeRecommendation(Recommendation recommendation) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(recommendation.getUserId());
		criteria.and("contentId").is(recommendation.getContentId());
		
		Query query = new Query(criteria);
		
		Update update = new Update();
		update.set("recycle", 1);
		mongoTemplate.updateFirst(query, update, Recommendation.class, Constants.MONGO_RECOMMENDATION_COL_NAME);
	}

	/**  
	 * 修改踩/赞记录
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param recommendation  
	 */ 
	@Override
	public void updateRecommendation(Recommendation recommendation) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(recommendation.getUserId());
		criteria.and("contentId").is(recommendation.getContentId());
		
		Update update = new Update();
		update.set("userId", recommendation.getUserId());
		update.set("contentId", recommendation.getContentId());
		update.set("choice", recommendation.getChoice());
		update.set("recycle", recommendation.getRecycle());
		update.set("updateTime", DateUtils.getCurrentTime());
		Query query = new Query(criteria);
		mongoTemplate.findAndModify(query, update, Recommendation.class, Constants.MONGO_RECOMMENDATION_COL_NAME);
		
	}

	/**
	 * 统计踩/赞总数 
	 * <p>Title: countAAndV</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param contentId
	 * @return
	 */
	@Override
	public Map<String, Integer> countAAndV(String contentId) {
		
		Criteria criteria = new Criteria();
		criteria.and("contentId").is(contentId);
		Query query = new Query(criteria);
		
		ContentRecommendation contentRecommendation = mongoTemplate.findOne(query, ContentRecommendation.class,
				Constants.MONGO_CONTENTRECOMMENDATION_COL_NAME);
		
		Map<String, Integer> map = new HashMap<>();
		
		if (!Utils.objIsNull(contentRecommendation)) {
			map.put("A", contentRecommendation.getTotalA());
			map.put("V", contentRecommendation.getTotalV());
		}else {
			map.put("A", 0);
			map.put("V", 0);	
		}
		
		return map;
	}

	/**  
	 * <p>Title: addComment</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param comment
	 * @return  
	 */ 
	@Override
	public Comment addComment(Comment comment) {
		
		mongoTemplate.insert(comment, Constants.MONGO_COMMENT_COL_NAME);
		return comment;
	}

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
	public List<Comment> getComment(String contentId, String commentId, Integer begin, Integer pagesize) {
		
		Criteria criteria = new Criteria();
		criteria.and("contentId").is(contentId);
		if (!Utils.objIsNull(commentId)) {
			criteria.and("id").is(commentId);
			
			Query query = new Query(criteria);
			List<Comment> list = mongoTemplate.find(query, Comment.class, Constants.MONGO_COMMENT_COL_NAME);
			
			//递归查询子评论
			List<Comment> resList = new ArrayList<>();
			resList.addAll(list);
			return getChildComment(list, resList);	
		}
		
		criteria.and("commentParentId").is("");
		return mongoTemplate.find(new Query(criteria).skip(begin).limit(pagesize), Comment.class, Constants.MONGO_COMMENT_COL_NAME);
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
			List<Comment> childList = mongoTemplate.find(new Query(criteria), Comment.class, Constants.MONGO_COMMENT_COL_NAME);
			
			if (!Utils.objIsNull(childList)) {	
				resList.addAll(childList);
				getChildComment(childList, resList);
			}
		}
		
		return resList;
	}
	
}
