/**
 * 
 */
package com.comment.service;

import java.util.List;
import java.util.Map;

import com.comment.model.Comment;
import com.comment.model.Recommendation;
import com.comment.model.Score;


/**
 * <p>Title: ReviewService</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月29日
 */
public interface ReviewService {
	
	/**
	 * 添加评分
	 * <p>Title: addScore</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param score
	 * @param userId
	 */
	void addScore(Score score, String userId);

	/**  
	 * 计算平均分 
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param contentId
	 * @return  
	 */ 
	Float calculateAvgScore(String contentId);

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
	Score getScore(String userId, String contentId);

	/**  
	 * 添加踩/赞  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param userId
	 * @param recommendation  
	 */ 
	void addRecommendation(String userId, Recommendation recommendation);

	/**
	 * 统计踩/赞总数
	 * <p>Title: countAAndV</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param contentId
	 * @return
	 */
	Map<String, Integer> countAAndV(String contentId);
	
	/**
	 * 查询用户踩/赞
	 * <p>Title: getRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param recommendation
	 * @return
	 */
	Recommendation getRecommendation(String userId, Recommendation recommendation);

	/**  
	 * 用户评论 
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param comment
	 * @return  
	 */ 
	Comment addComment(Comment comment);

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
	List<Comment> getComment(String contentId, String commentId, Integer begin, Integer pagesize);
}
