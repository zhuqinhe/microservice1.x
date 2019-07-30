package com.udcm.utils;

import java.util.HashMap;
import java.util.Map;

import com.udcm.common.ResponseCode;



public class ResponseUtils {

	public static Map<String, Object> createRespMap(ResponseCode ec) {
		Map<String, Object> respMap = new HashMap<>();
		respMap.put("resultCode", ec.getCode());
		respMap.put("description", ec.getName());
		return respMap;
	}
	public static Map<String,Object>updateRespMap(Map<String,Object>response,int resultCode,String description){
		if(response==null||response.isEmpty()){
			response.put("resultCode", resultCode);
			response.put("description", description);
		}
		response.put("resultCode", resultCode);
		response.put("description", description);
		return  response;
	}
	
}
