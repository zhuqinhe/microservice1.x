package com.comment.utils;

import java.util.HashMap;
import java.util.Map;

import com.comment.constants.ResponseCode;



public class ResponseUtils {

	public static Map<String, Object> createRespMap(ResponseCode ec) {
		Map<String, Object> respMap = new HashMap<>();
		respMap.put("resultCode", ec.getCode());
		respMap.put("description", ec.getName());
		return respMap;
	}
	public static Map<String,Object>updateRespMap(Map<String,Object>response,int resultCode,String description){
		if(response==null){
			response = new HashMap<>();
			response.put("resultCode", resultCode);
			response.put("description", description);
		}else if(response.isEmpty()){
			response.put("resultCode", resultCode);
			response.put("description", description);
		}else {
			response.put("resultCode", resultCode);
			response.put("description", description);
		}
		return  response;
	}
	
}
