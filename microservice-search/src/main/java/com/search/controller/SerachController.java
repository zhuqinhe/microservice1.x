/**
 * 
 */
package com.search.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.search.config.Config;
import com.search.constants.Constants;
import com.search.constants.ResponseCode;
import com.search.fegin.MetadataFeignClient;
import com.search.service.SearchService;
import com.search.utils.HttpUtils;
import com.search.utils.HttpUtils.Response;
import com.search.utils.JsonUtils;
import com.search.utils.ResponseUtils;
import com.search.utils.StringUtils;
import com.search.utils.Utils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:39:06
 */
@RestController
@RequestMapping("/")
public class SerachController {
	private Logger logger = LoggerFactory.getLogger(SerachController.class);

	@Resource
	private SearchService searchService;
	@Resource
	private MetadataFeignClient metadataFeignClient;

	@Resource
	private Config config;

	@GetMapping(value = "v1/search/keyword/{keyword}", produces = "application/json")
	public Map<String, Object> getMetadataBykeword(
			@PathVariable("keyword") String keyword,
			@RequestParam(value="begin",required=false) String begin,
			@RequestParam(value="pagesize",required=false) String pagesize, 
			@RequestParam(value="sp",required=false) String sp,
			@RequestParam(value="categories",required=false) String categories,
			HttpServletRequest request){
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		try {
			
			Object result=metadataFeignClient.getMediasBycontentId("00000001000000100003000000161610");
			List<Map<String,Object>>metadataMaps=(List<Map<String, Object>>) ((Map)result).get("metadataMap");
			response.put("metadataList",metadataMaps);
			response.put("totalRecords",metadataMaps.size());
			
            //通过搜索系统获取搜索内容
		  /*  String	searchUrl=config.getSearchUrl()+"/search/rest/v1/multiline/search";
			String json = "{\"queryText\":\"" + keyword + "\",\"start\":\"" + begin + "\",\"size\":\"" + pagesize + "\",\"fileName\":\"name_keyword\"}";
			Response res = HttpUtils.post(searchUrl, json,request);
			String resContent = (String) res.getContent(); 
			if(StringUtils.isNotEmpty(resContent)){
				try {
					Map map = JsonUtils.parseValue(resContent, Map.class);
					List<Map<String,Object>>contents=(List<Map<String,Object>>)map.get("contentList");
					if(contents!=null&&contents.size()>0){
						List<String>contentIds=new ArrayList<String>();
						for(Map tmap:contents){
							contentIds.add(tmap.get("contentId").toString());
						}
						//获取媒体内容，根据contentId 补全详情
						Object result=metadataFeignClient.getMediasBycontentId(String.join(",", contentIds));
						List<Map<String,Object>>metadataMaps=(List<Map<String, Object>>) ((Map)result).get("metadataMap");
						response.put("metadataList",metadataMaps);
						response.put("totalRecords",((Map)result).get("total"));
					}
				}catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}*/

		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}


	@GetMapping(value = "v1/search/actorname/{actorname}", produces = "application/json")
	public Map<String, Object> getMetadataByActorName(
			@RequestParam("synchronize")boolean synchronize,
			@PathVariable("keyword") String keyword,
			@RequestParam(value="begin",required=false) String begin,
			@RequestParam(value="pagesize",required=false) String pagesize, 
			@RequestParam(value="sp",required=false) String sp,
			@RequestParam(value="categories",required=false) String categories,
			HttpServletRequest request){
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.SUCCESS);
		try {



		} catch (Exception e) {
			ResponseUtils.updateRespMap(response, Constants.FAILURE, Constants.SERVER_EXCEPTION_DESCRIPTION);
			logger.error(e.getMessage(),e);
		}
		logger.debug(JsonUtils.obj2Json(response));
		return  response;
	}

}
