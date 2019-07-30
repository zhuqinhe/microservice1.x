package com.fonsview.sync.rest;


import com.fonsview.sync.service.CmsDataSyncService;
import com.fonsview.sync.utils.DateUtils;
import com.fonsview.sync.utils.JsonUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CmsDataSyncController {
    private Logger logger = LoggerFactory.getLogger(CmsDataSyncController.class);
    // private Gson gson = new Gson();
    @Autowired
    private CmsDataSyncService cmsDataSyncService;

    
    /**
     * 获取直播频道收视趋势
     * @param json
     * @return
     */
    @RequestMapping(value = "/v1/cms/service", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> getChannelTrend(@RequestBody String body){
    	logger.debug("getCmsData["+body+"]");
    	Map<String, Object> response = new HashMap<String, Object>();
    	try {
			response.put("ResultCode",0);
			response.put("Description", "OK");
			//解析请求的参数
			Map<String, Object> bodyMap = JsonUtils.json2Object(body, Map.class);
			String msgType = (String) bodyMap.get("MsgType");
			String startTime = (String) bodyMap.get("StartTime");
			String endTime = (String) bodyMap.get("EndTime");
			Integer start = (Integer) bodyMap.get("Start");
			Integer num = (Integer) bodyMap.get("Num");
			//处理查询结果
			response= cmsDataSyncService.prcessCmsData(msgType, DateUtils.dateFormatSTSC(startTime),DateUtils.dateFormatSTSC(endTime) , start, num,response);
		} catch (Exception e) {
			response.put("ResultCode", -1);
			response.put("Description", "unknown error");
			logger.error("getCmsData["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
    }
    
   
    @RequestMapping(value = "/v1/cms/service/dass", method = RequestMethod.POST, produces = "application/json")
    public Map<String, Object> getChanneldass(@RequestBody String body){
    	logger.debug("getCmsData["+body+"]");
    	Map<String, Object> response = new HashMap<String, Object>();
    	try {
			response.put("ResultCode",0);
			response.put("Description", "OK");
			//解析请求的参数
			Map<String, Object> bodyMap = JsonUtils.json2Object(body, Map.class);
			String msgType = (String) bodyMap.get("MsgType");
			String startTime = (String) bodyMap.get("StartTime");
			String endTime = (String) bodyMap.get("EndTime");
			Integer start = (Integer) bodyMap.get("Start");
			Integer num = (Integer) bodyMap.get("Num");
			//处理查询结果
			response= cmsDataSyncService.prcessCmsDataForDass(msgType, DateUtils.dateFormatSTSC(startTime),DateUtils.dateFormatSTSC(endTime) , start, num,response);
		} catch (Exception e) {
			response.put("ResultCode", -1);
			response.put("Description", "unknown error");
			logger.error("getCmsData["+body+"]");
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
    }
    
    @RequestMapping(value = "/v1/cms/service/udc", method = RequestMethod.GET, produces = "application/json")
    public Map<String, Object> getCmsDataForUDC(
    	@RequestParam("starttime")String startTime,
    	@RequestParam("endtime")String endTime,
    	@RequestParam("begin")int begin,
    	@RequestParam("pagesize")int pageSize,
    	@RequestParam("mediatype")int mediaType
    	
    	){
    	logger.debug(String.format("getCmsDataForUdc[startTime:%s,endTime:%s,begin:%s,pagesize:%s,mediatype:%s]",startTime, endTime, begin, pageSize,mediaType));
    	Map<String, Object> response = new HashMap<String, Object>();
    	try {
			response.put("ResultCode",0);
			response.put("Description", "OK");
			//处理查询结果
			List<Map<String,Object>>list=cmsDataSyncService.getCmsDataForUDC(startTime, endTime, begin, pageSize, mediaType);
			response.put("data", list);
		} catch (Exception e) {
			response.put("ResultCode", -1);
			response.put("Description", "unknown error");
	    	logger.error(String.format("getCmsDataForUdc[startTime:%s,endTime:%s,begin:%s,pagesize:%s,mediatype:%s]",startTime, endTime, begin, pageSize,mediaType));
			logger.error(e.getMessage(),e);
		}
    	logger.debug(JsonUtils.obj2Json(response));
		return  response;
    }
    
}
