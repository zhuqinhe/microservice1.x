package com.search.fegin;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.search.constants.ResponseCode;
import com.search.utils.ResponseUtils;


@Component
public class HystrixClientFallback implements MetadataFeignClient {

  @Override
  public Object getMediasBycontentId(String contentIds) {
		Map<String, Object> response = 	ResponseUtils.createRespMap(ResponseCode.ERROR);
		response.put("description","getMediasBycontentId is error,");
        return response;
  }
}