package com.zuul.filter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
@Component
public class ErrorFilter extends ZuulFilter {
	private Logger logger = Logger.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
    	
    	 return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
    	
        //需要在默认的 SendErrorFilter 之前
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        // 只有在抛出异常时才会进行拦截
        return RequestContext.getCurrentContext().containsKey("throwable");
    }

    @Override
    public Object run() {
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            Object e = requestContext.get("throwable");
            if (e != null && e instanceof ZuulException) {
                ZuulException zuulException = (ZuulException) e;
                // 删除该异常信息,不然在下一个过滤器中还会被执行处理
                requestContext.remove("throwable");
                // 响应给客户端信息
                HttpServletResponse response = requestContext.getResponse();
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pw = null;
                pw = response.getWriter();
                pw.write("{\"resultCode\": -1,\"description\": \"service error\",\"message\":\""+zuulException.getMessage()+"\"}");
                pw.close();
            }
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}