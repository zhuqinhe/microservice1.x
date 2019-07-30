package com.zuul.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
@Component  
/**
 * @Description  暂不启用特殊限流控制   默认每秒50000
 * @author hoob
 * @date 2019年7月19日下午4:03:56
 */
public class RateLimitFilter extends ZuulFilter {

	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	private static RateLimiter rateLimiter = RateLimiter.create(50000);//这里的1表示每秒允许处理的量为1个

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run()  {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();
			HttpServletResponse response = ctx.getResponse();
			if (!rateLimiter.tryAcquire()) {
				HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setStatus(httpStatus.value());
				response.getWriter().append(httpStatus.getReasonPhrase());
				response.setStatus(200);
				response.getWriter().write("{\"resultCode\": -1,\"description\": \"too many requests\"}");
				ctx.setSendZuulResponse(false);
				throw new ZuulException(
						httpStatus.getReasonPhrase(),
						httpStatus.value(),
						httpStatus.getReasonPhrase()
						);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
}
