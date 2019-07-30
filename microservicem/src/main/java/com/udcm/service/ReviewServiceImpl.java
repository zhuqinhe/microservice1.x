/**
 * 
 */
package com.udcm.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.udcm.dao.ReviewDao;
import com.udcm.model.Comment;
import com.udcm.model.ContentRecommendation;
import com.udcm.model.ContentScore;
import com.udcm.model.Recommendation;
import com.udcm.model.Reminder;
import com.udcm.model.Score;


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
	public List<Comment> getComment(String contentId, String userId, Integer begin, Integer pagesize) {

		return reviewDao.getComment(contentId, userId, begin, pagesize);
	}
	/**
	 * @Title getContentScore
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public List<ContentScore> getContentScore(String contentId,String name,int begin, int pageSize) {
		
		List<ContentScore>list= reviewDao.getContentScore(contentId,name,begin, pageSize);
		if(list!=null&&!list.isEmpty()){
			for(ContentScore cs:list){
				if(cs.getTotalNum()>0){
					cs.setAvgscore(cs.getTotalScore()/cs.getTotalNum());
				}else{
					cs.setAvgscore(0);
				}
			}
		}
		return list;
	}
	/**
	 * @Title getContentScoreCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getContentScoreCount(String contentId,String name) {
		return reviewDao.getContentScoreCount(contentId,name);
	}
	/**
	 * @Title getRecommendation
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public List<ContentRecommendation> getRecommendation(String contentId,String name,int begin, int pageSize) {
		return reviewDao.getRecommendation( contentId,name,begin, pageSize);
	}
	/**
	 * @Title getRecommendationCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getRecommendationCount(String contentId,String name) {
		
		return reviewDao.getRecommendationCount(contentId,name);
	}

	/**
	 * @Title getCommentCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getCommentCount(String contentId,String userId) {
	
		return reviewDao.getCommentCount(contentId,userId);
	}
	/**
	 * @Title deleteComment
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public void deleteComment(List<String> ids) {
		reviewDao.deleteComment(ids);		
	}
	/**
	 * @Title getReminder
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public List<Reminder> getReminder(String userId, int begin, int pageSize) {
		return reviewDao.getReminder(userId, begin, pageSize);
	}
	/**
	 * @Title getReminderCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getReminderCount(String userId) {
		return reviewDao.getReminderCount(userId);
	}
	/**
	 * @Title deleteReminder
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public void deleteReminder(List<String> ids) {
		
		reviewDao.deleteReminder(ids);
	}
	/**
	 * @Title getScore
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public List<Score> getScore(String userId, int begin, int pageSize) {
		
		return reviewDao.getScore(userId, begin, pageSize);
	}
	/**
	 * @Title getScoreCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getScoreCount(String userId) {
		
		return reviewDao.getScoreCount(userId);
	}
	/**
	 * @Title getRecommendations
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public List<Recommendation> getRecommendations(String userId, int begin,
			int pageSize) {
		
		return reviewDao.getRecommendations(userId, begin, pageSize);
	}
	/**
	 * @Title getRecommendationCount
	 * @Description 
	 * @param 
	 * @return ReviewService
	 * @throws 
	 */
	@Override
	public int getRecommendationCount(String userId) {
		
		return reviewDao.getRecommendationCount(userId);
	}
	
}
