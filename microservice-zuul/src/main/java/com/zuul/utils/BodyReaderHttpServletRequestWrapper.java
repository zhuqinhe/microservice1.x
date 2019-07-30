/**
 * 
 */
package com.zuul.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StreamUtils;

/**
 * @author Raul	
 * 2017年8月29日
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper{

	private Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	private byte[] body = null;
	
 	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			body = StreamUtils.copyToByteArray(request.getInputStream());
		} catch (IOException e) {			
			log.error(e);
		}
	}
	
 	@Override
 	public BufferedReader getReader() throws IOException { 		
 		return  new BufferedReader(new InputStreamReader(getInputStream()));
 	}
	
 	
 	@Override
 	public ServletInputStream getInputStream() throws IOException {
 		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
 		return new ServletInputStream() {
			
			@Override
			public int read() throws IOException {				
				return bais.read();
			}
			
			@Override
			public void setReadListener(ReadListener readListener) {				
			}
			
			@Override
			public boolean isReady() {				
				return false;
			}
			
			@Override
			public boolean isFinished() {				
				return false;
			}
		};
 	}
	
 	public String getRequestBody(){
 		return new String(body,Charset.forName("UTF-8"));
 	}

}
