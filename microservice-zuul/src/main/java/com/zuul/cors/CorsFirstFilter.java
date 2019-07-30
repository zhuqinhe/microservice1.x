package com.zuul.cors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.web.bind.annotation.RequestMethod;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

//@Component
public class CorsFirstFilter extends ZuulFilter {


	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		//// 优先级为0，数字越大，优先级越低
		return 0;
	}

	@Override
	public boolean shouldFilter() {

		RequestContext ctx = RequestContext.getCurrentContext();

		HttpServletRequest request = ctx.getRequest();

		//只过滤OPTIONS 请求

		if(request.getMethod().equals(RequestMethod.OPTIONS.name())){

			return true;

		}
	


		return false;

	}



	@Override

	public Object run() {

		RequestContext ctx = RequestContext.getCurrentContext();

		HttpServletResponse response = ctx.getResponse();


		response.setHeader("Access-Control-Allow-Origin","*");

		response.setHeader("Access-Control-Allow-Credentials","true");

		response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, deviceid, hmac, random, terminaltype, timestamp, timezone, usertoken");

		response.setHeader("Access-Control-Allow-Methods","GET, POST,DELETE, PUT");

		response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
		//不再路由
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(200);

		return null;

	}

}