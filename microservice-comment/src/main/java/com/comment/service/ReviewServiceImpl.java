/**
 * 
 */
package com.comment.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.comment.dao.ReviewDao;
import com.comment.model.Comment;
import com.comment.model.Recommendation;
import com.comment.model.Score;
import com.comment.utils.DateUtils;
import com.comment.utils.Utils;


/**
 * <p>Title: ReviewServiceImpl</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月29日
 */
@Service("reviewService")
public class ReviewServiceImpl implements ReviewService{
	@Resource
	private ReviewDao reviewDao;
	@Override
	public void addScore(Score score, String userId){
		score.setUserId(userId);
		score.setCreateTime(DateUtils.getCurrentTime());
		score.setUpdateTime(DateUtils.getCurrentTime());
		reviewDao.addScore(score);
	}
	/**  
	 * <p>Title: calculateAvgScore</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param contentId
	 * @return  
	 */ 
	@Override
	public Float calculateAvgScore(String contentId) {
		
		return reviewDao.getAvgScore(contentId);
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
		
		return reviewDao.getScore(userId,contentId);
	}
	/**  
	 * <p>Title: addRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param userId
	 * @param recommendation  
	 */ 
	@Override
	public void addRecommendation(String userId, Recommendation recommendation) {
		recommendation.setUserId(userId);
		recommendation.setCreateTime(DateUtils.getCurrentTime());
		recommendation.setUpdateTime(DateUtils.getCurrentTime());
		//查询当前用户踩/赞记录
		Recommendation dbRecommendation = reviewDao.getRecommendation(recommendation);
		
		if (Utils.objIsNull(dbRecommendation)) {
			reviewDao.addRecommendation(recommendation);
			
		}else {
			//相同操作，判定为取消
			if(recommendation.getChoice().equals(dbRecommendation.getChoice()) && dbRecommendation.getRecycle() == 0){
				reviewDao.removeRecommendation(recommendation);
			}else {
				reviewDao.updateRecommendation(recommendation);
			}
		}
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

		return reviewDao.countAAndV(contentId);
	}
	/**  
	 * 查询用户踩/赞
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param userId
	 * @param recommendation
	 * @return  
	 */ 
	@Override
	public Recommendation getRecommendation(String userId, Recommendation recommendation) {
		recommendation.setUserId(userId);

		return reviewDao.getRecommendation(recommendation);
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
		comment.setCreateTime(DateUtils.getCurrentTime());
		comment.setUpdateTime(DateUtils.getCurrentTime());
		
		return reviewDao.addComment(comment);
	}
	/**  
	 * 获取内容的评论 
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

		return reviewDao.getComment(contentId, commentId, begin, pagesize);
	}
	
}
