package com.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
public class BookmarkTestController {

	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;

	
	//收藏测试用例
	
	@GetMapping("/eureka-instance-bookmark")
	public String serviceMicroServiceBookmarkUrl() {
		//从注册发现服务中或取 收藏微服务的地址
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-BOOKMARK", false);
		return instance.getHomePageUrl();
	}
	@PostMapping(value = "v2/bookmark/add", produces = "application/json")
	public Object addUserBookmark(@RequestBody String body) {
		
	   return  restTemplate.postForObject("http://microservice-bookmark/v2/bookmark/add?synchronize="+false, body, Object.class);
		 
	}
	@GetMapping("/v2/bookmark/list")
	public Object  getUserBookmark(@RequestParam("userid")String userid) {
		
	   return  restTemplate.getForObject("http://microservice-bookmark/v2/bookmark/list?userid="+userid+"&synchronize="+false, Object.class);
		 
	}
	@GetMapping("/v2/bookmark/counts")
	public Object  getUserBookmarkCount(@RequestParam("userid")String userid) {
		
	   return  restTemplate.getForObject("http://microservice-bookmark/v2/bookmark/counts?userid="+userid+"&synchronize="+false, Object.class);
		 
	}
	@GetMapping("v2/bookmark/check/content")
	public Object  checkBookmark(@RequestParam("userid")String userid,@RequestParam("contentid")String contentid) {
		
	   return  restTemplate.getForObject("http://microservice-bookmark/v2/bookmark/check/content?userid="+userid+"&contentid="+contentid+"&synchronize="+false, Object.class);
		 
	}
	@DeleteMapping("/v2/bookmark/remove")
	public Object  deleteBookmark(@RequestParam("userid")String userid,
			@RequestParam(value="contentids",required=false)String contentids) {
		
	     restTemplate.delete("http://microservice-bookmark/v2/bookmark/remove?userid="+userid+"&pcontentids="+contentids+"&synchronize="+false, Object.class);
	     
	     return "OK";
		 
	}
	
	
	
	
	
}
