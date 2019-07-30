/**
 * 
 */
package com.comment.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comment.constants.Constants;
import com.comment.constants.ResponseCode;
import com.comment.model.Comment;
import com.comment.model.Recommendation;
import com.comment.model.Score;
import com.comment.service.ReviewService;
import com.comment.utils.JsonUtils;
import com.comment.utils.ResponseUtils;
import com.comment.utils.StringUtils;
import com.comment.utils.Utils;

/**
 * 点评服务
 * <p>Title: ReviewController</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月2日
 */
@RestController
@RequestMapping("/")
public class ReviewController {
	private Logger logger = LoggerFactory.getLogger(ReviewController.class);
	@Autowired
	private ReviewService reviewService;
	
	
	/**
	 * 添加评分  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param userId
	 * @param body
	 * @param request
	 * @return
	 */
	@PostMapping(value = "v2/user/score", produces = "application/json")
	public Map<String, Object> addScore(@RequestParam("userid") String userId,
			@RequestBody String body,HttpServletRequest request){
    	logger.debug("addScore["+body+"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("score", 0);
    	try {
    		if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		//参数校验
    		if (StringUtils.isEmpty(userId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		Score score=JsonUtils.json2Object(body,Score.class);
    		if (score == null) {
    			logger.error("Score is null");
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "Score is null");
    			return response;
    		}

    		if (StringUtils.isEmpty(score.getContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (score.getScore() < 1 || score.getScore() > 10) {
    			ResponseUtils.updateRespMap(response, Constants.FAILURE, "score is illegal");
    			return response;
    		}
    		
    		// userID不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
    		score.setUserId(userId);
    		//判断用户是否已经评分
    		Score dbScore = reviewService.getScore(userId, score.getContentId());
    		if (Utils.objIsNull(dbScore)) {
    			//添加评分
        		reviewService.addScore(score,userId);
			}else {
				ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.ALREADY_SCORED);
			}
    		
    		Float avgScore = reviewService.calculateAvgScore(score.getContentId());
    		response.put("score", avgScore);
    		
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("addScore["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	/**
	 * 获取内容平均分
	 * <p>Title: getAvgScore</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月2日   
	 * @param userId
	 * @param contentId
	 * @param request
	 * @return
	 */
	@GetMapping(value = "v2/content/score", produces = "application/json")
	public Map<String, Object> getAvgScore(@RequestParam(value = "userid", required = false) String userId,
			@RequestParam("contentid") String contentId, HttpServletRequest request){
    	logger.debug("getAvgScore[ userId:"+userId+", contentId" + contentId +" ]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("score", 0);
    	response.put("isScored", 0);
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}

    		if (StringUtils.isEmpty(contentId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		
    		//用户Id不为空 表示同时查询此用户是否已评分
    		if (!StringUtils.isEmpty(userId)) {
    			// userID不区分大小写，数据库存放的转化为小写。
        		userId = userId.toLowerCase();
    			Score dbScore = reviewService.getScore(userId, contentId);
    			//不为空，返回该用户评分
    			if (!Utils.objIsNull(dbScore)) {
    				response.put("isScored", 1);
    				response.put("userScore", dbScore.getScore());
				}
			}
    		
    		Float avgScore = reviewService.calculateAvgScore(contentId);
    		response.put("score", avgScore);
    		
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("getAvgScore[ userId:"+userId+", contentId" + contentId +"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	/**
	 * 用户踩/赞
	 * <p>Title: addRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param userId
	 * @param body
	 * @param request
	 * @return
	 */
	@PostMapping(value = "v2/user/recommendation", produces = "application/json")
	public Map<String, Object> addRecommendation(@RequestParam(value = "userid", required = true) String userId,
			@RequestBody String body,HttpServletRequest request){
    	logger.debug("addRecommendation["+body+"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("isA", false);
    	response.put("isV", false);
    	response.put("A", 0);
    	response.put("V", 0);
    	try {
    		if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		//参数校验
    		Recommendation recommendation=JsonUtils.json2Object(body,Recommendation.class);
    		if (StringUtils.isEmpty(userId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		if (recommendation == null) {
    			logger.error("Recommendation is null");
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "Recommendation is null");
    			return response;
    		}

    		if (StringUtils.isEmpty(recommendation.getContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(recommendation.getChoice()) ||
    				!("A".equals(recommendation.getChoice()) || "V".equals(recommendation.getChoice()))) {
    			ResponseUtils.updateRespMap(response, Constants.FAILURE, "choice is illegal");
    			return response;
    		}
    		
    		// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
    		Recommendation dbRecommendation = reviewService.getRecommendation(userId, recommendation);
    		if (!Utils.objIsNull(dbRecommendation)) {
				if (!recommendation.getChoice().equals(dbRecommendation.getChoice()) || dbRecommendation.getRecycle() == 1) {
					response.put(recommendation.getChoice().contains("V")?"isV":"isA", true);
				}
			}else {
				response.put(recommendation.getChoice().contains("V")?"isV":"isA", true);
			}
    		reviewService.addRecommendation(userId, recommendation);
    		Map<String, Integer> map = reviewService.countAAndV(recommendation.getContentId());
    		response.put("A", map.get("A") == null ? 0 : map.get("A"));
        	response.put("V", map.get("V") == null ? 0 : map.get("V"));
        	
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("addRecommendation["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	/**
	 * 获取内容踩/赞数
	 * <p>Title: countRecommendation</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param contentId
	 * @param request
	 * @return
	 */
	@GetMapping(value = "v2/content/recommendation", produces = "application/json")
	public Map<String, Object> countRecommendation(@RequestParam("contentid") String contentId, 
											HttpServletRequest request){
    	logger.debug("countRecommendation[ contentId" + contentId +" ]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("A", 0);
    	response.put("V", 0);
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}

    		if (StringUtils.isEmpty(contentId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		
    		Map<String , Integer> map = reviewService.countAAndV(contentId);
    		response.put("A", map.get("A") == null ? 0 : map.get("A"));
        	response.put("V", map.get("V") == null ? 0 : map.get("V"));
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("countRecommendation[ contentId" + contentId +"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	/**
	 * 用户评论
	 * <p>Title: addComment</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param body
	 * @param request
	 * @return
	 */
	@PostMapping(value = "v2/user/comment", produces = "application/json")
	public Map<String, Object> addComment(@RequestBody String body,HttpServletRequest request){
    	logger.debug("addComment["+body+"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("isComment", false);
    	response.put("comment", null);
    	try {
    		if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		//参数校验
    		Comment comment=JsonUtils.json2Object(body,Comment.class);
    		if (comment == null) {
    			logger.error("Comment is null");
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "Comment is null");
    			return response;
    		}

    		if (StringUtils.isEmpty(comment.getContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(comment.getComment())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "comment is null");
    			return response;
    		}
    		if (comment.getComment().length() > 255) {
    			ResponseUtils.updateRespMap(response, Constants.FAILURE, "length exceeds limit");
    			return response;
			}
    		if (StringUtils.isEmpty(comment.getUserId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		
    		// userID不区分大小写，数据库存放的转化为小写。
    		comment.setUserId(comment.getUserId().toLowerCase());
    		Comment dbComment = reviewService.addComment(comment);
    		if (!Utils.objIsNull(dbComment)) {
    			response.put("isComment", true);
    			response.put("comment",dbComment);
			}
    		
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("addComment["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	/**
	 * 获取内容的评论
	 * <p>Title: getComment</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月3日   
	 * @param contentId
	 * @param commentId
	 * @param begin
	 * @param pagesize
	 * @param request
	 * @return
	 */
	@GetMapping(value = "v2/content/comment", produces = "application/json")
	public Map<String, Object> getComment(@RequestParam("contentid") String contentId, 
										  @RequestParam(value = "commentid", required = false) String commentId,
										  @RequestParam("begin") Integer begin,
										  @RequestParam("pagesize") Integer pagesize,
										  HttpServletRequest request){
    	logger.debug("getComment[ contentId:" + contentId +", commentId:"+ commentId + 
    					", begin:" + begin + ", pagesize"+ pagesize +" ]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("List<Comment>", null);
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		
    		//参数校验
    		if (StringUtils.isEmpty(contentId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (Utils.objIsNull(begin)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "begin is null");
    			return response;
    		}
    		if (Utils.objIsNull(pagesize)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "pagesize is null");
    			return response;
    		}
    		
    		List<Comment> list = reviewService.getComment(contentId,commentId,begin,pagesize);
    		response.put("List<Comment>", list);
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("getComment[ contentId" + contentId +"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
}
