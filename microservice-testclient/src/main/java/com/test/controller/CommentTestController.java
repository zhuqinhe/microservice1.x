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
public class CommentTestController {

	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;


	
	@GetMapping("/eureka-instance-comment")
	public String serviceMicroServiceTestUrl() {
		//从注册发现获取测试服务
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-COMMENT", false);
		return instance.getHomePageUrl();
	}
	

	@PostMapping(value = "v2/user/score", produces = "application/json")
	public Object addUserComentScore(@RequestParam("userid") String userId,
			@RequestBody String body) {
		
	   return  restTemplate.postForObject("http://microservice-comment/v2/user/score?synchronize="+false+"&userid="+userId, body, Object.class);
		 
	}
	@GetMapping("/v2/content/score")
	public Object  getUserContentScore(@RequestParam("contentid") String contentId) {
		
	   return  restTemplate.getForObject("http://microservice-comment/v2/content/score?contentid="+contentId, Object.class);
		 
	}
	
	
	@PostMapping(value = "v2/user/recommendation", produces = "application/json")
	public Object addRecommendation(@RequestParam(value = "userid", required = true) String userId,
			@RequestBody String body){
		
	   return  restTemplate.postForObject("http://microservice-comment/v2/user/recommendation?userid="+userId, body, Object.class);
		 
	}
	
	
	@GetMapping(value = "v2/content/recommendation", produces = "application/json")
	public Object countRecommendation(@RequestParam("contentid") String contentId){
		
	   return  restTemplate.getForObject("http://microservice-comment/v2/content/recommendation?contentid="+contentId, Object.class);
		 
	}
	@PostMapping(value = "v2/user/comment", produces = "application/json")
	public Object addComment(@RequestBody String body,HttpServletRequest request){
		
		   return  restTemplate.postForObject("http://microservice-comment/v2/user/comment", body, Object.class);

		 
	}
	
	
	@GetMapping(value = "v2/content/comment", produces = "application/json")
	public Object getComment(@RequestParam("contentid") String contentId){
		   return  restTemplate.getForObject("http://microservice-comment/v2/content/comment?contentid="+contentId+"&begin="+0+"&pagesize="+20, Object.class);

	}
	
	
}
