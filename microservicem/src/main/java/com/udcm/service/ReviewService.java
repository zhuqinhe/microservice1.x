/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.Comment;
import com.udcm.model.ContentRecommendation;
import com.udcm.model.ContentScore;
import com.udcm.model.Recommendation;
import com.udcm.model.Reminder;
import com.udcm.model.Score;


/**
 * <p>Title: ReviewService</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月29日
 */
public interface ReviewService {
	

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
	List<Comment> getComment(String contentId, String userId, Integer begin, Integer pagesize);
	
	
	/**
	 * @Title getContentScore
	 * @Description 获取评分列表
	 * @param 
	 * @return List<ContentScore>
	 * @throws 
	 */
	List<ContentScore>getContentScore(String contentId,String name,int begin,int pageSize);
	
	/**
	 * @Title getContentScoreCount
	 * @Description 获取评分列表总数
	 * @param 
	 * @return int
	 * @throws 
	 */
	int getContentScoreCount(String contentId,String name);
	/**
	 * @Title getContentScore
	 * @Description 获取评分列表
	 * @param 
	 * @return List<ContentScore>
	 * @throws 
	 */
	List<ContentRecommendation>getRecommendation(String contentId,String name,int begin,int pageSize);
	
	/**
	 * @Title getContentScoreCount
	 * @Description 获取评分列表总数
	 * @param 
	 * @return int
	 * @throws 
	 */
	int getRecommendationCount(String contentId,String name);

	
	/**
	 * @Title getContentScoreCount
	 * @Description 获取评分列表总数
	 * @param 
	 * @return int
	 * @throws 
	 */
	int getCommentCount(String contentId, String userId);
	/**
	 * @Title deleteComment
	 * @Description 
	 * @param 
	 * @return ReviewDao
	 * @throws 
	 */
	public void deleteComment(List<String> ids);
	/**
	 * @Title getReminder
	 * @Description 获取提醒列表
	 * @param 
	 * @return List<Reminder>
	 * @throws 
	 */
	public List<Reminder>getReminder(String userId,int begin,int pageSize);
	
	/**
	 * @Title getReminderCount
	 * @Description 获取提醒总数
	 * @param 
	 * @return int
	 * @throws 
	 */
	public int getReminderCount(String userId);
	
	/**
	 * @Title deleteReminder
	 * @Description 删除提醒
	 * @param 
	 * @return void
	 * @throws 
	 */
	public void deleteReminder(List<String> ids) ;
	

	public List<Score>getScore(String userId,int begin,int pageSize);
	
	public int getScoreCount(String userId);
	
	public List<Recommendation>getRecommendations(String userId,int begin,int pageSize);
	
	public int getRecommendationCount(String userId);
}
