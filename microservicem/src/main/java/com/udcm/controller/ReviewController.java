package com.udcm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udcm.constants.StatusCode;
import com.udcm.model.Comment;
import com.udcm.model.ContentRecommendation;
import com.udcm.model.ContentScore;
import com.udcm.model.Recommendation;
import com.udcm.model.Reminder;
import com.udcm.model.Score;
import com.udcm.service.ReviewService;
import com.udcm.utils.StringUtils;
import com.udcm.vo.BatchRequest;
import com.udcm.vo.ListResponse;
import com.udcm.vo.Response;
@RestController
@RequestMapping("/ui")
public class ReviewController {
	private static Logger log = LogManager.getLogger(ReviewController.class.getName());
	@Resource
	private ReviewService reviewService;


	/**
	 *获取评分列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/score/list")
	public ListResponse<ContentScore> getList(
			@RequestParam("contentId") String contentId,
			@RequestParam("name") String name,
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize) {
		ListResponse<ContentScore> response = new ListResponse<ContentScore>(StatusCode.UI.UI_0);
		try {
			contentId=StringUtils.handleStrParam(contentId);
			name=StringUtils.handleStrParam(name);
			List<ContentScore>list=reviewService.getContentScore(contentId,name,begin, pageSize);
			int total=reviewService.getContentScoreCount(contentId,name);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	
	/**
	 * @Title getAVList
	 * @Description 获取踩赞统计数据数据
	 * @param 
	 * @return ListResponse<Recommendation>
	 * @throws 
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/av/list")
	public ListResponse<ContentRecommendation> getAVList(
			@RequestParam("contentId") String contentId,
			@RequestParam("name") String name,
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize) {
		ListResponse<ContentRecommendation> response = new ListResponse<ContentRecommendation>(StatusCode.UI.UI_0);
		try {
			contentId=StringUtils.handleStrParam(contentId);
			name=StringUtils.handleStrParam(name);
			List<ContentRecommendation>list=reviewService.getRecommendation(contentId,name,begin, pageSize);
			int total=reviewService.getRecommendationCount(contentId,name);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	
	/**
	 * @Title getAVList
	 * @Description 获取踩赞统计数据数据
	 * @param 
	 * @return ListResponse<Recommendation>
	 * @throws 
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/comment/list")
	public ListResponse<Comment> getCommentList(
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("contentId") String contentId,
			@RequestParam("userId") String userId
			) {
		ListResponse<Comment> response = new ListResponse<Comment>(StatusCode.UI.UI_0);
		userId=StringUtils.handleStrParam(userId);
		contentId=StringUtils.handleStrParam(contentId);
		try {
			List<Comment>list=reviewService.getComment(contentId,userId,begin, pageSize);
			int total=reviewService.getCommentCount(contentId,userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	
	@PostMapping(value = "/v1/review/user/comment/delete",produces = "application/json")
	public Response  deleteComment(@RequestBody BatchRequest batchRequest) {
		Response response = new Response(StatusCode.UI.UI_0);
		try {
			List<String>ids=batchRequest.getIds();
			if(ids==null||ids.isEmpty()){
				response.setResultCode(StatusCode.UI.UI_1);
				return response;
			}
			reviewService.deleteComment(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/reminder/list")
	public ListResponse<Reminder> getReminderList(
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("userId") String userId
			) {
		ListResponse<Reminder> response = new ListResponse<Reminder>(StatusCode.UI.UI_0);
		userId=StringUtils.handleStrParam(userId);
		try {
			List<Reminder>list=reviewService.getReminder(userId, begin, pageSize);
			int total=reviewService.getReminderCount(userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	
	@PostMapping(value = "/v1/review/user/reminder/delete",produces = "application/json")
	public Response  deleteReminder(@RequestBody BatchRequest batchRequest) {
		Response response = new Response(StatusCode.UI.UI_0);
		try {
			List<String>ids=batchRequest.getIds();
			if(ids==null||ids.isEmpty()){
				response.setResultCode(StatusCode.UI.UI_1);
				return response;
			}
			reviewService.deleteReminder(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/scorelist/list")
	public ListResponse<Score> geScoreList(
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("userId") String userId
			) {
		ListResponse<Score> response = new ListResponse<Score>(StatusCode.UI.UI_0);
		userId=StringUtils.handleStrParam(userId);
		try {
			List<Score>list=reviewService.getScore(userId, begin, pageSize);
			int total=reviewService.getScoreCount(userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	@RequestMapping(method = RequestMethod.GET, path = "/v1/review/user/avlist/list")
	public ListResponse<Recommendation> geRList(
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("userId") String userId
			) {
		ListResponse<Recommendation> response = new ListResponse<Recommendation>(StatusCode.UI.UI_0);
		userId=StringUtils.handleStrParam(userId);
		try {
			List<Recommendation>list=reviewService.getRecommendations(userId, begin, pageSize);
			int total=reviewService.getRecommendationCount(userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
}
