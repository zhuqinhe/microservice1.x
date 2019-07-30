/**
 * 
 */
package com.reminder.controller;

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
import com.reminder.constants.Constants;
import com.reminder.constants.ResponseCode;
import com.reminder.model.Reminder;
import com.reminder.service.ReminderService;
import com.reminder.utils.JsonUtils;
import com.reminder.utils.ResponseUtils;
import com.reminder.utils.StringUtils;
import com.reminder.utils.Utils;


/**
 * 提醒服务
 * <p>Title: ReminderController</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月4日
 */
@RestController
@RequestMapping("/")
public class ReminderController {
	private Logger logger = LoggerFactory.getLogger(ReminderController.class);
	@Autowired
	private ReminderService reminderService;
	
	
	/**
	 * 添加提醒
	 * <p>Title: addReminder</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param body
	 * @param request
	 * @return
	 */
	@PostMapping(value = "v1/user/reminder", produces = "application/json")
	public Map<String, Object> addReminder(@RequestBody String body,HttpServletRequest request){
    	logger.debug("addReminder["+body+"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	response.put("isReminder", false);
    	
    	try {
    		if(Utils.verifyHMAC(request, body)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		//参数校验
    		Reminder reminder=JsonUtils.json2Object(body,Reminder.class);
    		if (reminder == null) {
    			logger.error("Remind is null");
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "Remind is null");
    			return response;
    		}

    		if (StringUtils.isEmpty(reminder.getContentId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(reminder.getName())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "name is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(reminder.getUserId())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		if (StringUtils.isEmpty(reminder.getMediaType())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "mediaType is null");
    			return response;
    		}
    		//点播追剧时，当前看到第几集不能为空
    		if (StringUtils.isEmpty(reminder.getCurrentEpisodes()) && Constants.VOD.equals(reminder.getMediaType())) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "currentEpisodes is null");
    			return response;
    		}
    		//媒体类型为点播
    		if (Constants.VOD.equals(reminder.getMediaType())) {
    			response.put("currentEpisodes",reminder.getCurrentEpisodes());
    			//获取更新至第几集
    			//从数据库中查询当前更新到第几集
    			String updateEpisodes = "0";
    			//MediaData mediaData = reminderService.getMediaData(reminder);
			    /*	if (!Utils.objIsNull(mediaData) && !Utils.objIsNull(mediaData.getNowNumber())) {
					updateEpisodes = mediaData.getNowNumber();
				}*/
        		response.put("updateEpisodes", updateEpisodes);
        		
        		//更新集数  > 用户当前观看集数时，设置提醒
    			if(Integer.valueOf(updateEpisodes) > (Integer.valueOf(reminder.getCurrentEpisodes()))){
    				response.put("isReminder", true);
    			}
			}else if(Constants.LIVE.equals(reminder.getMediaType())){
				if (StringUtils.isEmpty(reminder.getReminderStrategy())) {
    				ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "reminderstrategy is null");
    				return response;
				}
    			if (!"1".equals(reminder.getReminderStrategy()) && !"2".equals(reminder.getReminderStrategy())) {
    				ResponseUtils.updateRespMap(response, Constants.FAILURE, "reminderstrategy is illegal");
    				return response;
				}
				response.put("isReminder", true);
				response.put("reminderstrategy", reminder.getReminderStrategy());
			}
    		
    		reminderService.addReminder(reminder);
        	
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("addRemind["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	/**
	 * 取消提醒
	 * <p>Title: removeRemind</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月8日   
	 * @param contentId
	 * @param userId
	 * @param mediaType
	 * @param request
	 * @return
	 */
	@DeleteMapping(value = "/v1/user/removereminder", produces = "application/json")
	public Map<String, Object> removeReminder(@RequestParam("contentid") String contentId, 
										  @RequestParam("userid") String userId,
										  @RequestParam("mediatype") String mediaType,
										  HttpServletRequest request){
    	logger.debug("removereminder[ contentId:" + contentId +", userId:"+ userId + ", mediaType:" + mediaType +"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response, Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}
    		
    		//参数校验
    		/*if (StringUtils.isEmpty(contentId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "contentId is null");
    			return response;
    		}*/
    		if (Utils.objIsNull(userId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "userId is null");
    			return response;
    		}
    		/*if (Utils.objIsNull(mediaType)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, "mediaType is null");
    			return response;
    		}*/
    		
    		reminderService.removeReminder(contentId,userId,mediaType);
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("removereminder[ contentId:" + contentId +", userId:"+ userId + 
					", mediaType:" + mediaType +"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
	
	
	
	/**
	 * 获取用户提醒列表
	 * <p>Title: reminderList</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月8日   
	 * @param userId
	 * @param contentId
	 * @param mediaType
	 * @param request
	 * @return
	 */
	@GetMapping(value = "v1/user/reminder/list", produces = "application/json")
	public Map<String, Object> reminderList(@RequestParam("userid") String userId,
			@RequestParam(value = "contentid", required = false) String contentId,
			@RequestParam(value = "mediatype", required = false) String mediaType,
			HttpServletRequest request){
    	logger.debug("reminderList[ userId:"+userId+", contentId" + contentId +", mediaType" + mediaType +"]");
    	Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
    	try {
    		if(Utils.verifyHMAC(request, null)!=0){
    			ResponseUtils.updateRespMap(response,Constants.VERIFY_FAILURE, Constants.AUTHENTICATION_FAILED);
    			return response;
    		}

    		if (StringUtils.isEmpty(userId)) {
    			ResponseUtils.updateRespMap(response, Constants.REQUIRED_PARAMETER_MISSING, " userId is null");
    			return response;
    		}
    		
    		List<Reminder> list = reminderService.getReminderList(userId, contentId, mediaType);
    		response.put("reminders", list);
    	} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error("reminderList[ userId:"+userId+", contentId" + contentId +", mediaType" + mediaType +"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}
}
