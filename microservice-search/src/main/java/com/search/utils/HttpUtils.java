package com.search.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Security;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

/**
 * HTTP工具类
 * 
 * @author makefu
 * @date 2017年2月16日
 */
public class HttpUtils {

	static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	private static final int TIMEOUT = 60000; //60S

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	 * 
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	 * 
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	 * 192.168.1.100 
	 * 
	 * 用户真实IP为： 192.168.1.110 
	 * 
	 * @param request 
	 * @return 
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 发送post消息并返回对方回复的内容
	 * 
	 * @param url
	 * @param contentw
	 * @return
	 */
	public static Response post(String url, String json) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送post消息的超时时间
		log.debug("post - url = {}, json = {}", url, json);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/json");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (json != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(json);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("post -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}

	/**
	 * 发送Post消息，内容以application/xml的形式
	 * 
	 * @author faker
	 * @param url
	 * @param xml
	 * @return
	 */
	public static Response post4xml(String url, String xml) {
		final int TIMEOUT = 1000 * 60;// 发送post消息的超时时间
        long startTime=System.currentTimeMillis();
		log.debug("post - url = {}, xml = {}", url, xml);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/xml");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setRequestProperty("Accept", "application/xml");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (xml != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(xml);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}

				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("post4xml -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}

	/**
	 * 发送get消息并返回对方回复的内容
	 * 
	 * @param url
	 * @return
	 */
	public static Response get(String url) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送post消息的超时时间

		log.debug("get - url = {}", url);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("GET");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("get -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}

	public static class Response {
		// -1未知异常
		private int responseCode;
		private Object content;

		public Response(int responseCode, Object content) {
			super();
			this.responseCode = responseCode;
			this.content = content;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public Object getContent() {
			return content;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}

		public void setContent(Object content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return "Response [responseCode=" + responseCode + ", content=" + content + "]";
		}

	}

	
	/**
	 * 发送消息到CDN
	 * @param xmlURL
	 * @return
	 */
	public static Response sendSoapMessage(String target,SOAPMessage message) {
		Response resp = new Response(404,null);		
		if(StringUtils.isEmpty(target)){
			log.error("Send target is null or empty");
			return resp;
		}
		SOAPConnection connection = null;
		try {
			// 创建连接  
			SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();  
			connection = soapConnFactory.createConnection();  
			
			URL url = new URL(target);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			//uc.setRequestMethod("GET");
			//uc.setRequestProperty("charset", "UTF-8");
			//uc.setDoOutput(true);
			//uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);            
            
            // 响应消息  
            SOAPMessage reply = connection.call(message, uc.getURL());
            
            // 创建soap消息转换对象  
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();  
            // Extract the content of the reply
            Source sourceContent = reply.getSOAPPart().getContent();  
            // Set the output for the transformation  
            
            log.debug("SOAP Message-->{}",sourceContent);                   
             
            resp.setContent(reply);
            resp.setResponseCode(200);
            
            return resp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp.setContent(e.getMessage());
            resp.setResponseCode(500);			
		}finally{
			try {
				// Close the connection 关闭连接	             
				if(connection!=null) {
					connection.close();
				}
			} catch (SOAPException e) {	
				log.error(e.getMessage(), e);				
			}	
		}
		return resp;
	}
	
	/**
	 * 发送post消息并返回对方回复的内容
	 * 
	 * @param url
	 * @param contentw
	 * @return
	 */
	public static Response post(String url, String json,HttpServletRequest request) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送post消息的超时时间
		
		log.debug("post - url = {}, json = {}", url, json);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/json");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			setHeaders(uc,request);
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (json != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(json);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("post -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}
	
	/**
	 * AAA请求，封装http请求头
	 * @param uc
	 */
	private static void setHeaders(HttpURLConnection uc,HttpServletRequest request){
		uc.setRequestProperty("userToken", request.getHeader("userToken"));
		uc.setRequestProperty("terminalType", request.getHeader("terminalType"));
		uc.setRequestProperty("deviceId", request.getHeader("deviceId"));
		uc.setRequestProperty("timestamp", request.getHeader("timestamp"));
		uc.setRequestProperty("timezone", request.getHeader("timezone"));
		uc.setRequestProperty("random", request.getHeader("random"));
		uc.setRequestProperty("hmac", request.getHeader("hmac"));
		uc.setRequestProperty("sp", "");
		
	}
	
	private static int getTimeOut(){
		int timeOut = 1000 * 1;
		/*String time = Utils.getEPGConfig(ConfigKey.HTTPTIMEOUT.toString());
		try {
			if(StringUtils.isNotEmpty(time)){
				timeOut = Integer.parseInt(time)*1000;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}*/
		return timeOut;
	}
	/**
	 * 发送get消息并返回对方回复的内容
	 * 
	 * @param url
	 * @return
	 */
	public static Response get(String url,HttpServletRequest request) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送消息的超时时间

		log.debug("get - url = {}", url);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("GET");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			setHeaders(uc, request);
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("get -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}
	
	/**
	 * <p>Title: delete</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月21日   
	 * @param url
	 * @return
	 */
	public static Response delete(String url) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送post消息的超时时间

		log.debug("delete - url = {}", url);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("DELETE");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug("delete -url={}, response = {},times={}ms", url,response,(endTime-startTime));
		return response;
	}
	
	private static String charset = "ISO-8859-1";

	
	private static final String terminalType = "2";
	private static final String timezone = TimeZone.getDefault().getID();
	
	
	
	
	
	/**
	 * AAA请求，封装http请求头
	 * @param uc
	 */
	private static void setHeaders(HttpURLConnection uc, Map<String, String> header){
		uc.setRequestProperty("userToken", header.get("userToken"));
		uc.setRequestProperty("terminalType", header.get("terminalType"));
		uc.setRequestProperty("deviceId", header.get("deviceId"));
		uc.setRequestProperty("timestamp", header.get("timestamp"));
		uc.setRequestProperty("timezone", header.get("timezone"));
		uc.setRequestProperty("random", header.get("random"));
		uc.setRequestProperty("hmac", header.get("hmac"));
		uc.setRequestProperty("sp", "");
	}
	
	public static Response get(String url, Map<String, String> header) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送消息的超时时间
		log.debug(String.format("get - url = %s", url));
		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("GET");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			setHeaders(uc, header);
			
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug(String.format("get -url=%s, response = %s,times=%sms", url,response,(endTime-startTime)));
		return response;
	}
	
	/**
	 * 发送post消息并返回对方回复的内容
	 * 
	 * @param url
	 * @param contentw
	 * @return
	 */
	public static Response post(String url, String json, Map<String, String> header) {
        long startTime=System.currentTimeMillis();
		int TIMEOUT = getTimeOut();// 发送post消息的超时时间
		
		log.debug(String.format("post - url = %s, json = %s", url, json));

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/json");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			uc.setRequestProperty("Connection", "Close");
			setHeaders(uc, header);
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (json != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(json);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		long endTime=System.currentTimeMillis();
		log.debug(String.format("post -url=%s, response = %s,times=%sms", url,response,(endTime-startTime)));
		return response;
	}
}
