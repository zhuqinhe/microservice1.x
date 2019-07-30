package com.search.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "microservice-metadata", fallback = HystrixClientFallback.class)
public interface MetadataFeignClient {
  @RequestMapping(value = "/v1/media/{contentIds}/details", method = RequestMethod.GET)
  public Object getMediasBycontentId(@PathVariable("contentIds")String contentIds);
}
