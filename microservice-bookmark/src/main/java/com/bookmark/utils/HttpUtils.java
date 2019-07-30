package com.bookmark.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookmark.constants.Method;


public class HttpUtils {
	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	private static int rTimeout = 3 * 1000;
	private static int cTimeout = 3 * 1000;
	private static String MEDIA_XML_TYPE = "application/xml";

	public static String doXmlRequest(String url, String body) throws Exception {
		return doRequest(Method.POST, url, body, MEDIA_XML_TYPE);
	}

	/**
	 * HTTP接口访问
	 * 
	 * @param method
	 * @param url
	 * @param content
	 * @param headers
	 * @return
	 * @throws MalformedURLException
	 * @throws SDKException
	 */
	public static String doRequest(Method method, String url, String content, String contentType) throws Exception {

		log.debug(">>>>> doRequest start.url = " + url);
		if (null == method || StringUtils.isBlank(url)) {
			log.error(">>> http method or url is null,url = {}", url);
			return null;
		}

		StringBuffer sb = new StringBuffer();
		OutputStreamWriter osw = null;
		BufferedReader in = null;

		int code = 200;
		URL urls = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
		uc.setRequestMethod(method.name());
		uc.setRequestProperty("Content-Type", contentType);
		uc.setRequestProperty("Charset", "UTF-8");
		uc.setRequestProperty("Connection", "close");
		uc.setDoOutput(true);
		uc.setDoInput(true);
		uc.setReadTimeout(rTimeout);
		uc.setConnectTimeout(cTimeout);

		// 此处要对流指定字符集，否则服务端接收会有中文件乱码
		// uc.getOutputStream()需要和HttpURLConnection 在同一次请求里面。需要放置的到前面。
		// uc.getOutputStream()方法调用会发送请求，前面只是建立TCP/IP连接。
		if (Method.DELETE.equals(method)) {
			content = null;
		}
		if (content != null) {
			osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
			osw.write(content);
			osw.flush();
		}

		code = uc.getResponseCode();
		if (code >= 200 && code < 300) {
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
					sb.append(System.getProperty("line.separator"));
				}
				in.close();
			}

		} else {
			log.error(">>> request error, url = {}, httpcode = {}", url, code);
		}

		return sb.toString();
	}
	/**
	 * HTTP接口访问
	 * 
	 * @param method
	 * @param url
	 * @param content
	 * @param headers
	 * @return
	 * @throws MalformedURLException
	 * @throws SDKException
	 */
	public static String doJsonRequest(Method method, String url, String content) throws Exception {

		log.debug(">>>>> doRequest start.url = " + url);
		if (null == method || StringUtils.isBlank(url)) {
			log.error(">>> http method or url is null,url = {}", url);
			return null;
		}

		StringBuffer sb = new StringBuffer();
		OutputStreamWriter osw = null;
		BufferedReader in = null;

		int code = 200;
		URL urls = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
		//uc.setChunkedStreamingMode(0);//超时不重复请求
		uc.setRequestMethod(method.name());
		uc.setRequestProperty("Content-Type","application/json");
		uc.setRequestProperty("Charset", "UTF-8");
		uc.setRequestProperty("Connection", "close");
		uc.setDoOutput(true);
		uc.setDoInput(true);
		uc.setReadTimeout(rTimeout);
		uc.setConnectTimeout(cTimeout);

		// 此处要对流指定字符集，否则服务端接收会有中文件乱码
		// uc.getOutputStream()需要和HttpURLConnection 在同一次请求里面。需要放置的到前面。
		// uc.getOutputStream()方法调用会发送请求，前面只是建立TCP/IP连接。
		if (Method.DELETE.equals(method)) {
			content = null;
		}
		if (content != null) {
			osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
			osw.write(content);
			osw.flush();
		}

		code = uc.getResponseCode();
		if (code >= 200 && code < 300) {
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
					sb.append(System.getProperty("line.separator"));
				}
				in.close();
			}

		} else {
			log.error(">>> request error, url = {}, httpcode = {}", url, code);
		}

		return sb.toString();
	}
	/**
	 * HTTP接口访问
	 * 
	 * @param method
	 * @param url
	 * @param content
	 * @param headers
	 * @return
	 * @throws MalformedURLException
	 * @throws SDKException
	 */
	public static String doJsonRequest(Method method, String url,String content ,Integer timeout) throws Exception {

		log.debug(">>>>> doRequest start.url = " + url);
		if (null == method || StringUtils.isBlank(url)) {
			log.error(">>> http method or url is null,url = {}", url);
			return null;
		}

		StringBuffer sb = new StringBuffer();
		OutputStreamWriter osw = null;
		BufferedReader in = null;

		int code = 200;
		URL urls = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
		//uc.setChunkedStreamingMode(0);//超时不重复请求
		uc.setRequestMethod(method.name());
		uc.setRequestProperty("Content-Type","application/json");
		uc.setRequestProperty("Charset", "UTF-8");
		uc.setRequestProperty("Connection", "close");
		uc.setDoOutput(true);
		uc.setDoInput(true);
		if(timeout!=null){
			uc.setReadTimeout(timeout);
			uc.setConnectTimeout(timeout);
		}else{
			uc.setReadTimeout(rTimeout);
			uc.setConnectTimeout(cTimeout);
		}
		// 此处要对流指定字符集，否则服务端接收会有中文件乱码
		// uc.getOutputStream()需要和HttpURLConnection 在同一次请求里面。需要放置的到前面。
		// uc.getOutputStream()方法调用会发送请求，前面只是建立TCP/IP连接。
		if (Method.DELETE.equals(method)) {
			content = null;
		}
		if (content != null) {
			osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
			osw.write(content);
			osw.flush();
		}

		code = uc.getResponseCode();
		if (code >= 200 && code < 300) {
			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
					sb.append(System.getProperty("line.separator"));
				}
				in.close();
			}

		} else {
			log.error(">>> request error, url = {}, httpcode = {}", url, code);
		}

		return sb.toString();
	}

}
