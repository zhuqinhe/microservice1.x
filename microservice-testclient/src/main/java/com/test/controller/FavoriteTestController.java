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
public class FavoriteTestController {

	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;


	/**
	 * @Title serviceMicroServiceTestUrl
	 * @Description 
	 * @param 
	 * @return String
	 * @throws 
	 */
	@GetMapping("/test")
	public String testOk() {
		return "the test microservice is good";
	}  

	@GetMapping("/eureka-instance-test")
	public String serviceMicroServiceTestUrl() {
		//从注册发现获取测试服务
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-TEST", false);
		return instance.getHomePageUrl();
	}
	
	//收藏测试用例
	
	@GetMapping("/eureka-instance-favorite")
	public String serviceMicroServiceFavoriteUrl() {
		//从注册发现服务中或取 收藏微服务的地址
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-FAVORITE", false);
		return instance.getHomePageUrl();
	}
	@PostMapping(value = "v2/favorite/add", produces = "application/json")
	public Object addUserFavorite(@RequestBody String body) {
		
	   return  restTemplate.postForObject("http://microservice-favorite/v2/favorite/add?synchronize="+false, body, Object.class);
		 
	}
	@GetMapping("/v2/favorite/list")
	public Object  getUserFavorite(@RequestParam("userid")String userid) {
		
	   return  restTemplate.getForObject("http://microservice-favorite/v2/favorite/list?userid="+userid+"&synchronize="+false, Object.class);
		 
	}
	@GetMapping("/v2/favorite/counts")
	public Object  getUserFavoriteCount(@RequestParam("userid")String userid) {
		
	   return  restTemplate.getForObject("http://microservice-favorite/v2/favorite/counts?userid="+userid+"&synchronize="+false, Object.class);
		 
	}
	@GetMapping("v2/favorite/check/content")
	public Object  checkFavorite(@RequestParam("userid")String userid,@RequestParam("contentid")String contentid) {
		
	   return  restTemplate.getForObject("http://microservice-favorite/v2/favorite/check/content?userid="+userid+"&contentid="+contentid+"&synchronize="+false, Object.class);
		 
	}
	@DeleteMapping("/v2/favorite/remove")
	public Object  deleteFavorite(@RequestParam("userid")String userid,
			@RequestParam(value="contentids",required=false)String contentids,
			@RequestParam("mediatype") String mediaType) {
		
	     restTemplate.delete("http://microservice-favorite/v2/favorite/remove?userid="+userid+"&contentids="+contentids+"&mediatype="+mediaType+"&synchronize="+false, Object.class);
	     
	     return "OK";
		 
	}
	
	
	
	
	
}
