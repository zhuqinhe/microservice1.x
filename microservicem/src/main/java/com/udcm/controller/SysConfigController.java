package com.udcm.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParseException;
import com.udcm.common.ConfigKey;
import com.udcm.constants.StatusCode;
import com.udcm.model.SysConfig;
import com.udcm.service.SysConfigService;
import com.udcm.utils.JsonUtils;
import com.udcm.utils.StringUtils;
import com.udcm.vo.ListResponse;
import com.udcm.vo.VoResponse;
@RestController
@RequestMapping("/ui")
public class SysConfigController {
	private static Logger log = LogManager.getLogger(SysConfigController.class.getName());
	@Resource
	private SysConfigService sysConfigService;

	/**
	 * 获取系统配置列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/list")
	public ListResponse getList(HttpServletRequest request) {
		// log.debug("get config list ");
		ListResponse response = new ListResponse(0);

		long total = 0;
		try {

			List<SysConfig> sysConfigList = sysConfigService.getConfigList();
			if (null != sysConfigList && !sysConfigList.isEmpty()) {
				response.setList(sysConfigList);
				response.setTotal(0);// 该查询不显示总数，置为0只是占位
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new ListResponse(-1);
		}

		return response;
	}
	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/detail")
	public VoResponse<SysConfig> get(@RequestParam("key") String key, HttpServletRequest request) {
		// log.debug("get config ");
		VoResponse response = new VoResponse(0);
		key=StringUtils.handleStrParam(key);
		SysConfig sysConfigVo = null;
		try {
			if (key != null) {
				sysConfigVo = sysConfigService.findConfig(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		response.setVo(sysConfigVo);
		return response;
	}

	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/bykey")
	public VoResponse<SysConfig> getConfigByKey(@RequestParam("key") String key) {
		// log.debug("get config ");
		VoResponse<SysConfig> response = new VoResponse<SysConfig>(0);
		key=StringUtils.handleStrParam(key);
		SysConfig sysConfig = null;
		try {
			if (StringUtils.isNotEmpty(key)) {
					sysConfig = sysConfigService.findConfig(key);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
		}

		response.setVo(sysConfig);
		return response;
	}
	/**
	 * 更新配置内容
	 * 
	 * @param ftpServerVo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/update", consumes = {
			"application/json" }, produces = { "application/json" })
	public VoResponse update(@RequestBody SysConfig sysConfig) {
		// log.debug("update ftp ");
		VoResponse response = new VoResponse(0);
		try {
			sysConfigService.updateInfo(sysConfig);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		return response;
	}


	/**
	 * 启停配置
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/enable")
	public VoResponse enableConfig(@RequestParam(value = "key", required = false) String key) {
		// log.debug("enable ");
		VoResponse response = new VoResponse(0);
		key= StringUtils.handleStrParam(key);
		try {
			sysConfigService.enableConfig(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		return response;
	}

	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/language")
	public VoResponse getLanguage() {
		log.debug("get language ");
		VoResponse response = new VoResponse(0);
		SysConfig sysConfig = null;
		try {
			sysConfig = sysConfigService.findConfig(ConfigKey.LANGS_CONFIG.name());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		response.setVo(sysConfig.getValue());
		return response;
	}

	
	
	
	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/value")
	public VoResponse getValue(@RequestParam(value = "key", required = true) String keyStr) {
		log.debug("get value ");
		VoResponse response = new VoResponse(0);
		SysConfig sysConfig = null;

		sysConfig = sysConfigService.findConfig(keyStr.toUpperCase());
		if (null != sysConfig) {
			try {
				response.setVo(sysConfig.getValue());
			} catch (JsonParseException e) {
				response.setVo(sysConfig.getValue());
			}catch(IllegalArgumentException e){
				response.setVo(sysConfig.getValue());
			}catch(Exception e){
				log.error(e.getMessage(), e);
				response = new VoResponse(-1);
			}
		}

		return response;
	}
	/**
	 * 获取配置详情 JSON格式 不支持非JSON 
	 * 
	 * @author Jack
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping( path = "/v1/sys/config/json",produces="application/json")
	public VoResponse getJSON(@RequestParam(value = "key", required = true) String key) {
		SysConfig sysConfig = sysConfigService.findConfig(key);
		if (null != sysConfig) {
			VoResponse response = new VoResponse();
			response.setResultCode(StatusCode.UI.UI_0);
			response.setDescription("JSON is unusefull");
			response.setVo(JsonUtils.json2Obj(sysConfig.getValue(), Map.class));//获取配置详情 JSON格式 不支持非JSON 
		}
		VoResponse response = new VoResponse();
		response.setResultCode(StatusCode.UI.UI_1);
		response.setDescription("JSON is unusefull");
		return response;
	}
}
