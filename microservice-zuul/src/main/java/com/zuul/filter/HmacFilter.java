package com.zuul.filter;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zuul.config.Config;
import com.zuul.utils.BodyReaderHttpServletRequestWrapper;
import com.zuul.utils.HmacUtil;

/**
 * 登录过滤器
 *记得类上加Component注解
 */
@Component
public class HmacFilter extends ZuulFilter {
   @Autowired
   private Config config;
    /**
     * 过滤器类型，前置过滤器
     */
    @Override
    public String filterType() {
    	 /*

        pre：可以在请求被路由之前调用

        route：在路由请求时候被调用

        post：在route和error过滤器之后被调用

        error：处理请求时发生错误时被调用

        * */

        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器顺序，越小越先执行
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * 过滤器是否生效
     * 返回true代表需要权限校验，false代表不需要用户校验即可访问
     */
    @Override
    public boolean shouldFilter() {

        //共享RequestContext，上下文对象，需要权限校验URL
       /* RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if ("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        } else if ("/apigateway/order/api/v1/order/list".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        } else if ("/apigateway/order/api/v1/order/find".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }*/
    	//是否需要进行hmac校验
    	
        return true;
    }

    /**
     * 业务逻辑
     * 只有上面返回true的时候，才会进入到该方法
     */
    @Override
    public Object run() {
    	//统一hmac 校验
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //token对象,有可能在请求头传递过来，也有可能是通过参数传过来，实际开发一般都是请求头方式
        BodyReaderHttpServletRequestWrapper warpper = new BodyReaderHttpServletRequestWrapper(request);
		String body = warpper.getRequestBody();		
        //登录校验逻辑  如果token为null，则直接返回客户端，而不进行下一步接口调用
        if (!HmacUtil.checkHMAC(request,body,config)) {
          
            requestContext.setSendZuulResponse(false);
         
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}