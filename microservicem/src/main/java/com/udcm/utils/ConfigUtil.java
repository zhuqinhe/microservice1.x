package com.udcm.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ConfigUtil {
	 public static Map<String,String>config=new ConcurrentHashMap<String,String>();
	 public static String getProperties(String key){
		 if(StringUtils.isEmpty(key)){
			 return null;
		 }else{
			return  config.get(key.toLowerCase());
		 }
	 }
	/*

	private static final Logger log = Logger.getLogger(ConfigUtil.class);
	public static final Properties PROPERTIES = new Properties();
	private static File file = null;
	
	 public static void init()  {
		FileReader reader = null;
		try {
			file = new File(Constants.SYNC_DATA_ETC_PATH+"config.properties");
			reader = new FileReader(file);
			PROPERTIES.load(reader);
		} catch (Exception e) {
			log.error("init config.properties exception.", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
	}
	 public static String getProperties(String key){
		 if(StringUtils.isEmpty(key)){
			 return null;
		 }else{
			return  PROPERTIES.getProperty(key.toLowerCase());
		 }
	 }
*/}
