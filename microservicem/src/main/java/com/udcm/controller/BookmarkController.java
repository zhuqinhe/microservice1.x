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
import com.udcm.model.Bookmark;
import com.udcm.model.NeedToDelete;
import com.udcm.service.BookmarkService;
import com.udcm.utils.StringUtils;
import com.udcm.vo.BatchRequest;
import com.udcm.vo.ListResponse;
import com.udcm.vo.Response;

@RestController
@RequestMapping("/ui")
public class BookmarkController {
	private static Logger log = LogManager.getLogger(BookmarkController.class.getName());
	@Resource
	private BookmarkService bookmarkService;


	/**
	 * 获取系统配置列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/bookmark/list")
	public ListResponse<Bookmark> getList(
			@RequestParam("userId") String userId,
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize) {
		ListResponse<Bookmark> response = new ListResponse<Bookmark>(StatusCode.UI.UI_0);
		try {
			userId=StringUtils.handleStrParam(userId);
			List<Bookmark>list=bookmarkService.getBookmarkList(userId, begin, pageSize);
			long total=bookmarkService.getCounts(userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
	@PostMapping(value = "/v1/bookmark/delete",produces = "application/json")
	public Response  delete(@RequestBody BatchRequest batchRequest) {
		Response response = new Response(StatusCode.UI.UI_0);
		try {
			List<String>ids=batchRequest.getIds();
			if(ids==null||ids.isEmpty()){
				response.setResultCode(StatusCode.UI.UI_1);
				return response;
			}
			bookmarkService.remove(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);
		}
		return response;
	}
	
	/**
	 * 获取系统配置列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/userid/list")
	public ListResponse<NeedToDelete> getListUserIds(
			@RequestParam("userId") String userId,
			@RequestParam("begin") int begin,
			@RequestParam("pageSize") int pageSize) {
		ListResponse<NeedToDelete> response = new ListResponse<NeedToDelete>(StatusCode.UI.UI_0);
		try {
			userId=StringUtils.handleStrParam(userId);
			List<NeedToDelete>list=bookmarkService.getUserIds(userId,begin, pageSize);
			long total=bookmarkService.getUserIdCount(userId);
			response.setList(list);
			response.setTotal(total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(StatusCode.UI.UI_1);;
		}
		return response;
	}
}
