/**
 * 
 */
package com.bookmark.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bookmark.constants.Constants;
import com.bookmark.constants.ResponseCode;
import com.bookmark.model.Bookmark;
import com.bookmark.service.BookmarkService;
import com.bookmark.utils.JsonUtils;
import com.bookmark.utils.ResponseUtils;
import com.bookmark.utils.StringUtils;
import com.bookmark.utils.Utils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:39:06
 */
@RestController
@RequestMapping("/")
public class BookmarkController {
	private Logger logger = LoggerFactory.getLogger(BookmarkController.class);
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private RestTemplate restTemplate;
	/**
	 * 添加书签
	 * <p>Title: addBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param bookmark	书签
	 * @return
	 */
	@PostMapping(value = "v2/bookmark/add", produces = "application/json")
	public Map<String, Object> addBookmark(@RequestParam("synchronize")boolean synchronize,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestBody String body,HttpServletRequest request){
    	logger.debug("addBookmark["+body+"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	try {
    		if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		//参数校验
    		Bookmark bookmark=JsonUtils.json2Object(body,Bookmark.class);
    		if (bookmark == null) {
    			logger.error("bookmark is null");
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "bookmark is null");
    			return response;
    		}

    		if (StringUtils.isEmpty(bookmark.getContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(bookmark.getName())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "name is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(bookmark.getpContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "pContentId   is null");
    			return response;
    		}
    		if (Utils.objIsNull(bookmark.getIndex())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "index is null");
    			return response;
    		}
    		if (Utils.objIsNull(bookmark.getTime())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "time is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(bookmark.getUserId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		// userID不区分大小写，数据库存放的转化为小写。
    		bookmark.setUserId(bookmark.getUserId().toLowerCase());
    		bookmarkService.add(bookmark,synchronize,groupType);

    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("addBookmark["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	/**
	 * 获取用户书签
	 * <p>Title: getBookmarkList</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param userId	用户id
	 * @param begin		开始行数,为空是从0开始
	 * @param pageSize	页大小，为空时表示所有内容,一般是三个月内
	 * @param sp		服务提供商
	 * @return			
	 */
	@GetMapping( value = "v2/bookmark/list")
	public Map<String, Object> getBookmarkList(@RequestParam("userid") String userId, 
			@RequestParam(value="begin",required=false) Integer begin, @RequestParam(value="pagesize",required=false) Integer pageSize,
			@RequestParam(value="sp",required=false) String sp,
			@RequestParam(value="contentId",required=false) String contentId,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request) {
		logger.debug(String.format("getBookmarkList[userId:%s--begin:%s--pageSize:%s--sp:%s--groupType:%s--synchronize%s]",userId, begin, pageSize, sp,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		if (StringUtils.isEmpty((userId))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
				return response;
			}
    		groupType=StringUtils.handleStrParam(groupType);
    		
    		// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
			List<Bookmark> list = bookmarkService.getBookmarkList(userId, begin, pageSize, sp, contentId, groupType,synchronize);
			response.put("bookMarkList", list);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("getBookmarkList[userId:%s--begin:%s--pageSize:%s--sp:%s--groupType:%s--synchronize%s]",userId, begin, pageSize, sp,groupType,synchronize));		
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	/**
	 * 获取用户书签数
	 * <p>Title: getCounts</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "v2/bookmark/counts")
	public Map<String, Object> getCounts(@RequestParam("userid") String userId,HttpServletRequest request) {
		logger.debug(String.format("getCounts[userId:%s]",userId));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		
		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		if (StringUtils.isEmpty((userId))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
				return response;
			}
    		
    		// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
    		Integer counts = bookmarkService.getCounts(userId);
    		response.put("counts", counts);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("getCounts[userId:%ss]", userId));
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return response;
	}
	
	/**
	 * 删除书签
	 * <p>Title: removeBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param pContentIds
	 * @return
	 */
	@DeleteMapping( value = "v2/bookmark/remove")
	public Map<String, Object> removeBookmark(@RequestParam("userid") String userId, 
			@RequestParam("pcontentids") String pContentIds,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request) {
		logger.debug(String.format("removeBookmark[userid:%s--pcontentids:%s--grouptype:%s--synchronize:%s]", userId, pContentIds,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		
		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response, Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		if (StringUtils.isEmpty((userId))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
				return response;
			}
    		if (StringUtils.isEmpty((pContentIds))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "pContentIds is null");
				return response;
			}
    		
    		// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
    		bookmarkService.removeBookmark(userId, pContentIds,groupType, synchronize);
    		
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("removeBookmark[userid:%s--pcontentids:%s--grouptype:%s--synchronize:%s]", userId, pContentIds,groupType,synchronize));		
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return response;
	}
	
	/**
	 * 核对用户书签
	 * <p>Title: checkBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param contentId
	 * @return
	 */
	@GetMapping( value = "v2/bookmark/check/content")
	public Map<String, Object> checkBookmark(@RequestParam("userid") String userId,
			@RequestParam("contentid") String contentId,
			@RequestParam(value="index",required=false) Integer index,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request) {
		logger.debug(String.format("checkBookmark[userId:%s--contentId:%s--grouptype:%s--synchronize:%s]", userId, contentId,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		
		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		if (StringUtils.isEmpty((userId))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
				return response;
			}
    		if (StringUtils.isEmpty((contentId))) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
				return response;
			}
    		
    		// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
    		List<Map<String, Object>> resList = bookmarkService.checkBookmark(userId, contentId,index,groupType,synchronize);
    		
    		response.put("bookmarkStatusList", resList);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("checkBookmark[userId:%s--contentId:%s--grouptype:%s--synchronize:%s]", userId, contentId,groupType,synchronize));		
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return response;
	}
	@GetMapping( value = "/v2/getconfig")
	public Object getConfig(){
		Object obj=restTemplate.getForObject("http://microservice-config-server/bookmark-dev.yml", Object.class);
		return "1";
	}
}
