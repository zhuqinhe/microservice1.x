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
public class SyncDataTestController {

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
	@GetMapping("/eureka-instance-sync-data")
	public String serviceMicroServiceTestUrl() {
		//从注册发现获取测试服务
		InstanceInfo instance = this.eurekaClient.getNextServerFromEureka("MICROSERVICE-SYNC_DATA", false);
		return instance.getHomePageUrl();
	}
	
	
	@PostMapping(value = "v1/cms/service", produces = "application/json")
	public Object addUserReminder(@RequestBody String body) {
		
	   return  restTemplate.postForObject("http://microservice-sync-data/v1/cms/service", body, Object.class);
		 
	}
	
}
