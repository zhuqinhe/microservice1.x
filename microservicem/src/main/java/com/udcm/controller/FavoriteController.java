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
import com.udcm.model.Favorite;
import com.udcm.service.FavoriteService;
import com.udcm.utils.StringUtils;
import com.udcm.vo.BatchRequest;
import com.udcm.vo.ListResponse;
import com.udcm.vo.Response;

@RestController
@RequestMapping("/ui")
public class FavoriteController {
	private static Logger log = LogManager.getLogger(FavoriteController.class.getName());
	@Resource
	private FavoriteService favoriteService;


	/**
	 * 获取系统配置列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/favorite/list")
	public ListResponse<Favorite> getList(
			@RequestParam("userId") String userId,
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize) {
		ListResponse<Favorite> response = new ListResponse<Favorite>(StatusCode.UI.UI_0);
		
		try {
			userId=StringUtils.handleStrParam(userId);
			List<Favorite>list=favoriteService.getFavoriteList(userId, begin, pageSize);
			       long   total=favoriteService.getFavoriteCount(userId, null);
			       response.setList(list);
			       response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	@PostMapping(value = "/v1/favorite/delete",produces = "application/json")
	public Response  delete(@RequestBody BatchRequest batchRequest) {
		Response response = new Response(StatusCode.UI.UI_0);
		try {
			List<String>ids=batchRequest.getIds();
			if(ids==null||ids.isEmpty()){
				response.setResultCode(StatusCode.UI.UI_1);
				return response;
			}
			favoriteService.remove(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
		return response;
	}
}
