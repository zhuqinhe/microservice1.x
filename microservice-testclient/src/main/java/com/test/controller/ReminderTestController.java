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
public class ReminderTestController {

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
	@GetMapping("/eureka-instance-reminder")
	public String serviceMicroServiceTestUrl() {
		//从注册发现获取测试服务
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-REMINDER", false);
		return instance.getHomePageUrl();
	}
	
	
	@PostMapping(value = "v1/user/reminder", produces = "application/json")
	public Object addUserReminder(@RequestBody String body) {
		
	   return  restTemplate.postForObject("http://microservice-reminder/v1/user/reminder", body, Object.class);
		 
	}
	@DeleteMapping(value = "/v1/user/removereminder", produces = "application/json")
	public Object removeReminder(@RequestParam("contentid") String contentId, 
										  @RequestParam("userid") String userId,
										  @RequestParam("mediatype") String mediaType){
	     restTemplate.delete("http://microservice-reminder/v1/user/removereminder?userid="+userId+"&contentid="+contentId+"&mediatype="+mediaType);
		 return "{'resultCode':0}";
	}
	@GetMapping("v1/user/reminder/list")
	public Object  getUserReminder(@RequestParam("userid")String userid) {
		
	   return  restTemplate.getForObject("http://microservice-reminder/v1/user/reminder/list?userid="+userid, Object.class);
		 
	}
	
	
	
	
	
	
}
