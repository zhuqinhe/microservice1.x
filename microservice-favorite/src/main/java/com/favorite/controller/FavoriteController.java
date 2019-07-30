package com.favorite.controller;




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
import com.favorite.constants.Constants;
import com.favorite.constants.ResponseCode;
import com.favorite.model.Favorite;
import com.favorite.service.FavoriteService;
import com.favorite.utils.JsonUtils;
import com.favorite.utils.ResponseUtils;
import com.favorite.utils.StringUtils;
import com.favorite.utils.Utils;

@RestController
@RequestMapping("/")
public class FavoriteController {    
	private Logger logger = LoggerFactory.getLogger(FavoriteController.class);

	@Autowired
	private FavoriteService favoriteService;

	
	/*public Map<String, Object> fallbackMethodWithaddFavorite(@RequestParam("synchronize")boolean synchronize,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestBody String body,HttpServletRequest request){
		Map<String, Object> response =ResponseUtils.createRespMap(ResponseCode.ERROR);
		response.put("fallbackMethod", "addFavorite");
		response.put("synchronize", synchronize);
		response.put("body", body);
		response.put("groupType", groupType);
	    return response;	
	}*/
	/**
	 * 获取直播频道收视趋势
	 * @param json
	 * @return
	 */
	@PostMapping(value = "v2/favorite/add", produces = "application/json")
//	@HystrixCommand(fallbackMethod = "fallbackMethodWithaddFavorite")
	public Map<String, Object> addFavorite(@RequestParam("synchronize")boolean synchronize,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestBody String body,HttpServletRequest request){
		logger.debug("addFavorite["+body+"]");
		Map<String, Object> response =ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		try {
			if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
			
			Favorite favorite=JsonUtils.json2Object(body, Favorite.class);
			//解析请求的参数
			if(favorite==null){
				logger.error("favorite  is null ");
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "favorite  is null");
				return response;
			}
			if(StringUtils.isEmpty(favorite.getUserId())){
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
				return response;
			}
			if(StringUtils.isEmpty(favorite.getContentId())){
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
				return response;
			}
			if(StringUtils.isEmpty(favorite.getMediaType())){
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "mediaType is null");
				return response;
			}
			if(StringUtils.isEmpty(favorite.getName())){
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "name is null");
				return response;
			}
			// userID不区分大小写，数据库存放的转化为小写。
			favorite.setUserId(favorite.getUserId().toLowerCase());
			favoriteService.add(favorite,synchronize,groupType);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION );
			logger.error("addFavorite["+body+"]");
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	@GetMapping( value = "v2/favorite/list")
	public Map<String, Object> getFavoriteList(@RequestParam("userid") String userId, 
			@RequestParam(value="begin",required=false) Integer begin, 
			@RequestParam(value="pagesize",required=false) Integer pageSize,
			@RequestParam(value="date",required=false) String date,
			@RequestParam(value="mediatype",required=false) String mediaType,
			@RequestParam(value="sp",required=false) String sp,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request) {
		logger.debug(String.format("getFavoriteList[userId:%s--begin:%s--pageSize:%s--sp:%s--date:%s--mediatype:%s--groupType:%s--synchronize:%s]",
				userId, begin, pageSize, sp, date, mediaType,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
			if (StringUtils.isEmpty((userId))) {
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " userId is null");
				return response;
			}
			if(StringUtils.isEmpty(mediaType)){
				mediaType=Constants.DEFAULT_MEDIATYPE;
			}
			
			// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
			List<Favorite> list = favoriteService.getFavoriteList(userId, begin, pageSize, sp, date,mediaType,groupType,synchronize);
			response.put("favoriteList", list);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.debug(String.format("getFavoriteList[userId:%s--begin:%s--pageSize:%s--sp:%s--date:%s--mediatype:%s--groupType:%s--synchronize:%s]",
					userId, begin, pageSize, sp, date, mediaType,groupType,synchronize));	
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	@GetMapping( value = "v2/favorite/counts")
	public Map<String, Object> getFavoriteCount(@RequestParam("userid") String userId, 
			@RequestParam(value="mediatype",required=false) String mediaType,HttpServletRequest request
			) {
		logger.debug(String.format("getFavoriteCount[userId:%s--mediaType:%s]",
				userId, mediaType));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);

		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
			if (StringUtils.isEmpty((userId))) {
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " userId is null");
				return response;
			}
			if(StringUtils.isEmpty(mediaType)){
				mediaType=Constants.DEFAULT_MEDIATYPE;
			}
			
			// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
			long count = favoriteService.getFavoriteCount(userId, mediaType);
			response.put("counts", count);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("getFavoriteCount[userId:%s--mediaType:%s]",
					userId, mediaType));
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}

	
	
	@DeleteMapping( value = "v2/favorite/remove")
	public Map<String, Object> removeFavorite(@RequestParam("userid") String userId, 
			@RequestParam(value="contentids",required=false) String contentdIs,
			@RequestParam(value="mediatype") String mediaType,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request
			) {
		logger.debug(String.format("removeFavorite[userId:%s--mediaType:%s--contentdIs:%s--groupType:%s--synchronize:%s]",userId, mediaType,contentdIs,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);

		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
			if (StringUtils.isEmpty((userId))) {
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " userId is null");
				return response;
			}
			// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
			favoriteService.remove(userId, contentdIs, mediaType,groupType,synchronize);
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("removeFavorite[userId:%s--mediaType:%s--contentdIs:%s--groupType:%s--synchronize:%s]",userId, mediaType,contentdIs,groupType,synchronize));		
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	@GetMapping( value = "v2/favorite/check/content")
	public Map<String, Object> checkFavorite(@RequestParam("userid") String userId, 
			@RequestParam("contentid") String contentId,			
			@RequestParam(value="mediatype",required=false) String mediaType,
			@RequestParam(value="grouptype",required=false) String groupType,
			@RequestParam("synchronize")boolean synchronize,
			HttpServletRequest request
			) {
		logger.debug(String.format("checkFavorite[userId:%s--contentId:%s--grouptype:%s--synchronize:%s]",userId, contentId,groupType,synchronize));		
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);

		try {
			if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
			if (StringUtils.isEmpty((userId))) {
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " userId is null");
				return response;
			}
			if (StringUtils.isEmpty((contentId))) {
				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " contentdIs is null");
				return response;
			}
			/*if(StringUtils.isEmpty(mediaType)){
				mediaType=Constants.DEFAULT_MEDIATYPE;
			}*/
			// userId不区分大小写，数据库存放的转化为小写。
    		userId = userId.toLowerCase();
			if(favoriteService.checkFavorite(userId, contentId,mediaType,groupType,synchronize)){
				response.put("isFavorited",1);
			}else{
				response.put("isFavorited",0);
			}
		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(String.format("checkFavorite[userId:%s--contentId:%s--grouptype:%s--synchronize:%s]",userId, contentId,groupType,synchronize));		
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
}
