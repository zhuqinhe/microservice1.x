/**
 * 
 */
package com.fonsview.metadata.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fonsview.metadata.constants.Constants;
import com.fonsview.metadata.constants.StatusCode;
import com.fonsview.metadata.service.MetadataService;
import com.fonsview.metadata.utils.CacheUtils;
import com.fonsview.metadata.utils.HttpUtils;
import com.fonsview.metadata.utils.MapUtils;
import com.fonsview.metadata.utils.StringUtils;
import com.fonsview.metadata.utils.Utils;


/**
 * @Description 
 * @author hoob
 * @date 2018年8月3日上午11:46:18
 */
@RestController
@RequestMapping
public class MetadataV1Controller {
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	private MetadataService metadataService; 
	
	/**
	 * @Title getChannelList
	 * @Description 获取标签列表
	 * @param language   语言
	 * @param type       标签类型
	 * @return LabelListResponse
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/channel/{type}/list",produces="application/json")
	public Object getChannelList(@PathParam(value="type") String type,
			@RequestParam(value="sp",required=false) String cpId,
			@RequestParam(value="filterBy",required=false) String filter,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    try {
		    	String key=String.format("metadata_getChannelList(%s,%s,%s)",cpId,type,filter);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
		    		List<Map<String,Object>>channls= metadataService.getChannelList(request, cpId, type, filter);
					result.put("resultCode",StatusCode.UI.UI_0);
					result.put("description",Constants.R_SUCCESS);
					result.put("channelList", channls);
					CacheUtils.setCacheData(key, result);
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("interface:v1/channel/{type}/list,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:type="+type
						+",@RequestParam:sp="+cpId
						+",@RequestParam:filterBy="+filter
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	/**
	 * @Title getSchedullList
	 * @Description 获取标签列表
	 * @param language   语言
	 * @param type       标签类型
	 * @return LabelListResponse
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/channel/{contentId}/schedules",produces="application/json")
	public Object getSchedullList(@PathVariable(value="contentId") String contentId,
			@RequestParam(value="date",required=false) String date,
			@RequestParam(value="days",required=false) String days,
			HttpServletRequest request){
	    	long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    try {
		    	String key=String.format("metadata_getSchedullList(%s,%s,%s)", contentId,date, days);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
					List<Map<String,Object>>channls= metadataService.getSchedulList(contentId,date, days);
					result.put("resultCode",StatusCode.UI.UI_0);
					result.put("description",Constants.R_SUCCESS);
					result.put("scheduleList", channls);
					CacheUtils.setCacheData(key, result);
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("interface:v1/channel/{type}/list,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId
						+",@RequestParam:date="+date
						+",@RequestParam:days="+days
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	/**
	 * @Title getCategoryListByContentId
	 * @Description 获取子栏目列表
	 * @param contentId   语言
	 * @return Object
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/category/{contentId}/categories",produces="application/json")
	public Object getCategoryListByContentId(
			@PathVariable(value="contentId") String contentId,
			@RequestParam(value="sp",required=false) String cpId,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentId=StringUtils.handleStrParam(contentId);
		    try {
		    	String key=String.format("metadata_getCategoryListByContentId(%s,%s)", contentId, cpId);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
		    		List<Map<String,Object>>categoryList= metadataService.getCategorysByContentId(contentId, cpId);
					result.put("resultCode",StatusCode.UI.UI_0);
					result.put("description",Constants.R_SUCCESS);
					String identifier=MapUtils.CONTENTIDORIDENTIFER.get(contentId);
					if(StringUtils.isEmpty(identifier)){
						Map<String,Object>map=metadataService.getCategorydetail(contentId);
						if(map!=null&&!map.isEmpty()){
							identifier=map.get("identifier")==null?"":map.get("identifier").toString();
							MapUtils.CONTENTIDORIDENTIFER.put(contentId, identifier);
							MapUtils.CONTENTIDORIDENTIFER.put(identifier, contentId);
						}
					}
					result.put("identifier", identifier);
					result.put("contentId", contentId);
					result.put("categoryList", categoryList);
					CacheUtils.setCacheData(key, result);
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("interface:v1/channel/{type}/list,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId
						+",@RequestParam:sp="+cpId
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	/**
	 * @Title getCategoryDetail
	 * @Description 获取标签列表
	 * @param contentId  
	 * @return Object
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/category/{contentId}/detail",produces="application/json")
	public Object getCategoryDetail(@PathVariable(value="contentId") String contentId,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentId=StringUtils.handleStrParam(contentId);
		    try {
		    	String key=String.format("metadata_getCategoryDetail(%s)", contentId);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
		    		Map<String,Object>category= metadataService.getCategorydetail(contentId);
					if(category!=null){
						result=category;
						result.put("resultCode",StatusCode.UI.UI_0);
						CacheUtils.setCacheData(key, result);
					}else{
						result.put("resultCode",StatusCode.UI.UI_0);
						result.put("description","category is null");
					}
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:v1/category/{contentId}/detail,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	/**
	 * @Title getMediaByCategory
	 * @Description 获取栏目内容（只支持合集和直播）（后续优化调整）
	 * @param 
	 * @return Object
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/category/{contentId}",produces="application/json")
	public Object getMediaByCategory(
			@PathVariable(value="contentId") String contentId,
			@RequestParam(value="begin",required=false) String begin,
			@RequestParam(value="pageSize",required=false) String pageSize,
			@RequestParam(value="filterBy",required=false) String filterBy,
			@RequestParam(value="sortBy",required=false) String sortBy,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentId=StringUtils.handleStrParam(contentId);
		    int begin_int=0;
		    int pageSize_int=10;
		    int totalRecords=0;
		    begin=StringUtils.handleStrParam(begin);
		    pageSize=StringUtils.handleStrParam(pageSize);
		    filterBy=StringUtils.handleStrParam(filterBy);
		    sortBy=StringUtils.handleStrParam(sortBy);
		    if(StringUtils.isNotEmpty(begin)){
		    	begin_int=StringUtils.handleIntParam(begin);
		    }
		    if(StringUtils.isNotEmpty(pageSize)){
		    	pageSize_int=StringUtils.handleIntParam(pageSize);
		    }
		    try {
		    	String key=String.format("metadata_getMediaByCategory(%s,%s,%s,%s,%s)", sortBy, filterBy, contentId, pageSize, begin);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
		    		totalRecords=metadataService.getMediaByCategoryWithSeriesCount( sortBy, filterBy, contentId);
			    	if(totalRecords==0){
			    		//没有合集内容，查询直播
			    		totalRecords=metadataService.getMediaByCategoryWithChannelCount(sortBy, filterBy, contentId);
			    		List<Map<String,Object>>contents= metadataService.getMediaByCategoryWithChannel(request, sortBy, filterBy, contentId, pageSize_int, begin_int);
					    result.put("resultCode",StatusCode.UI.UI_0);
					    result.put("totalRecords",totalRecords);
					    result.put("description",Constants.R_SUCCESS);
					    result.put("metadataList",contents);
			    	}
			    	else{
			    		List<Map<String,Object>>contents= metadataService.getMediaByCategoryWithSeries(request, sortBy, filterBy, contentId, pageSize_int, begin_int);
			    		 result.put("resultCode",StatusCode.UI.UI_0);
			    		 result.put("totalRecords",totalRecords);
						 result.put("description",Constants.R_SUCCESS);
						 result.put("metadataList",contents);
			    	}
			    	//栏目下关联的专题
			    	List<Map<String,Object>>specialTopicList=metadataService.getSpecialByCategory(contentId);
				    result.put("specialTopicList", specialTopicList);
				    CacheUtils.setCacheData(key, result);
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:v1/category/{contentId},ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/media/{contentId}/playurl",produces="application/json")
	public Object getVodPrayUrl(
			@PathVariable(value="contentId") String contentId,
			@RequestParam(value="mcontentids",required=false) String mcontentids,
			@RequestParam(value="authorizedToken",required=false) String token,
			@RequestParam(value="clientIp",required=false)String clientIp,
			@RequestParam(value="userId",required=false)String userId,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentId=StringUtils.handleStrParam(contentId);
		    try {
		    	  List<Map<String,Object>>urls=metadataService.getVODPlayUrl(request, contentId, mcontentids, clientIp, userId);
				  result.put("playInfoList", urls);
				  result.put("resultCode",StatusCode.UI.UI_0);
				  result.put("description",Constants.R_SUCCESS);
		   
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:v1/media/{contentId}/playurl,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId
						+",@RequestParam:mcontentids="+mcontentids
						+",@RequestParam:authorizedToken="+token
						+",@RequestParam:clientIp="+clientIp
						+",@RequestParam:userId="+userId
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	
	
	/**
	 * @Title getChannelPrayUrl
	 * @Description 获取直播播放地址
	 * @param 
	 * @return Object
	 * @throws 
	 */
	@RequestMapping(method=RequestMethod.GET,path="/v1/media/channel/playurl",produces="application/json")
	public Object getChannelPrayUrl(
			@RequestParam(value="contentids") String contentIds,
			@RequestParam(value="authorizedToken",required=false) String token,
			@RequestParam(value="clientIp",required=false)String clientIp,
			@RequestParam(value="userId",required=false)String userId,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentIds=StringUtils.handleStrParam(contentIds);
		    try {
		      List<Map<String,Object>>urls=metadataService.getChannelPlayUrl(request, contentIds, clientIp, userId);
			  result.put("playInfoList", urls);
			  result.put("resultCode",StatusCode.UI.UI_0);
			  result.put("description",Constants.R_SUCCESS);
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:v1/media/channel/playurl,ip="+HttpUtils.getIpAddr(request)
						+",@RequestParam:contentIds="+contentIds
						+",@RequestParam:authorizedToken="+token
						+",@RequestParam:clientIp="+clientIp
						+",@RequestParam:userId="+userId
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	
	
	/**
	 * @Title getChannelPrayUrl
	 * @Description 获取直播播放地址
	 * @param 
	 * @return Object
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/filters/{type}/list",produces="application/json")
	public Object getFilters(
			@PathVariable(value="type") String type,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    type=StringUtils.handleStrParam(type);
		    try {
		      String key=String.format("metadata_getFilters(%s)", type);
		      Object obj=CacheUtils.getCacheData(key);
		      if(obj!=null){
		    	  result=(Map<String, Object>) obj;
		      }else{
		    	      List<Map<String,Object>>filters=metadataService.getFilters(type);
					  result.put("filters", filters);
					  result.put("resultCode",StatusCode.UI.UI_0);
					  result.put("description",Constants.R_SUCCESS);
					  CacheUtils.setCacheData(key, result);
		      }
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:/v1/filters/{type}/list,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:type="+type
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	/**
	 * @Title getChannelPrayUrl
	 * @Description 获取直播播放地址
	 * @param 
	 * @return Object
	 * @throws 
	 */
	@RequestMapping(method=RequestMethod.GET,path="/v1/definition/{type}/list",produces="application/json")
	public Object getLabels(
			@PathVariable(value="type") String type,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    type=StringUtils.handleStrParam(type);
		    try {
		      List<Map<String,Object>>labels=metadataService.getLabels(type);
			  result.put("definitionList", labels);
			  result.put("resultCode",StatusCode.UI.UI_0);
			  result.put("description",Constants.R_SUCCESS);
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:/v1/definition/{type}/list,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:type="+type
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	
	
	/**
	 * @Title getChannelPrayUrl
	 * @Description 获取直播播放地址
	 * @param 
	 * @return Object
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/media/{contentIds}/details",produces="application/json")
	public Object getMediasBycontentId(
			@PathVariable(value="contentIds") String contentIds,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentIds=StringUtils.handleStrParam(contentIds);
		    try {
		      String key=String.format("metadata_getMediasBycontentId(%s)", contentIds);
		      Object obj=CacheUtils.getCacheData(key);
		      if(obj!=null){
		    	  result=(Map<String, Object>) obj;
		      }else{
		    	  List<Map<String,Object>>filters=metadataService.getMediasBycontentId(contentIds);
				  result.put("metadataMap", filters);
				  result.put("resultCode",StatusCode.UI.UI_0);
				  result.put("description",Constants.R_SUCCESS);
				  CacheUtils.setCacheData(key, result);
		      }
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:/v1/media/{contentIds}/details,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentIds="+contentIds
						);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET,path="/v1/media/{contentId}/detail",produces="application/json")
	public Object getMediaDetailBycontentId(
			@PathVariable(value="contentId") String contentId,
			@RequestParam(value="code",required=false) String code,
			HttpServletRequest request){
		    long startTime=System.currentTimeMillis();
		    Map<String,Object>result=new HashMap<String,Object>();
		    contentId=StringUtils.handleStrParam(contentId);
		    code=StringUtils.handleStrParam(code);
		    try {
		    	String key =String.format("metadata_getMediaDetailBycontentId(%s,%s)", contentId, code);
		    	Object obj=CacheUtils.getCacheData(key);
		    	if(obj!=null){
		    		result=(Map<String, Object>) obj;
		    	}else{
		    		   Map<String,Object>media=metadataService.getMediaDetailBycontentId(contentId, code);
		 			  result.put("metadata", media);
		 			  result.put("resultCode",StatusCode.UI.UI_0);
		 			  result.put("description",Constants.R_SUCCESS);
		 			  CacheUtils.setCacheData(key, result);
		    	}
			} catch (Exception e) {
				result.put("resultCode",StatusCode.UI.UI_1);
				result.put("description",Constants.R_FAIL);
				logger.error("url:/v1/media/{contentId}/detail,ip="+HttpUtils.getIpAddr(request)
						+",@PathVariable:contentId="+contentId);
				logger.error(e.getMessage(),e);
			}
		    return result;
	}
}
