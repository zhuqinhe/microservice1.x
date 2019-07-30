package com.zuul.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.exception.HystrixTimeoutException;

@Component
public class ZuulFallbackProvider implements FallbackProvider   {
	private Logger logger = Logger.getLogger(ZuulFallbackProvider.class);

@Override
public String getRoute() {
    return "*";
}

@Override
public ClientHttpResponse fallbackResponse(Throwable cause) {
    logger.error(String.format("route:%s,exceptionType:%s,stackTrace:%s", this.getRoute(), cause.getClass().getName(), cause.getStackTrace()));
    String message = "";
    if (cause instanceof HystrixTimeoutException) {
        message = "Timeout";
    } else {
        message = "Service exception";
    }
    return fallbackResponse(message);
}

public ClientHttpResponse fallbackResponse(String message) {

    return new ClientHttpResponse() {
        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.getStatusCode().value();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.getStatusCode().getReasonPhrase();
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() throws IOException {
            String bodyText = String.format("{\"resultCode\":-1,\"description\": \"Service unavailable:%s\"}", message);
            return new ByteArrayInputStream(bodyText.getBytes());
        }

        @Override
        public HttpHeaders getHeaders() {
        	  HttpHeaders headers = new HttpHeaders();
              MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
              headers.setContentType(mt);
              return headers;
        }
    };

}

/**
 * @Title fallbackResponse
 * @Description 
 * @param 
 * @return ZuulFallbackProvider
 * @throws 
 */
@Override
public ClientHttpResponse fallbackResponse() {
	return new ClientHttpResponse() {
        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.getStatusCode().value();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.getStatusCode().getReasonPhrase();
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() throws IOException {
            String bodyText = String.format("{\"resultCode\":-1,\"description\": \"Service unavailable\"}");
            return new ByteArrayInputStream(bodyText.getBytes());
        }

        @Override
        public HttpHeaders getHeaders() {
        	  HttpHeaders headers = new HttpHeaders();
              MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
              headers.setContentType(mt);
              return headers;
        }
    };

}


}
