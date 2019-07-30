/**
 * 
 */
package com.fonsview.metadata.utils;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

/**
 * @Description 
 * @author hoob
 * @date 2018年3月28日上午10:40:09
 */
public class CacheUtils {
	static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	// 数据缓存
	//expireAfterAccess: 当缓存项在指定的时间段内没有被读或写就会被回收。
	//expireAfterWrite：当缓存项在指定的时间段内没有更新就会被回收。
	//refreshAfterWrite：当缓存项上一次更新操作之后的多久会被刷新。
	private final static Cache<String, Object> contentCache = CacheBuilder.newBuilder().
			maximumSize(100000).expireAfterWrite(10, TimeUnit.MINUTES).recordStats().build();
	
   public static Object getCacheData(String key){
	  return contentCache.getIfPresent(key);
   }
   public static void setCacheData(String key,Object obj){
	   contentCache.put(key, obj);
   }
   
	/**
	 * 获取缓存统计信息
	 * @return
	 */
	public static CacheStats getCacheStats () {
		return contentCache.stats();
	}
	
	/**
	 * 获取缓存大小
	 * @return
	 */
	public static long getCacheSize() {
		return contentCache.size();
	}
	
	/**
	 * 清除缓存
	 * @return
	 */
	public static void clearCache(String key) {
		contentCache.invalidate(key);
	}
	/**
	 * 清除缓存
	 * @return
	 */
	public static void clearCache() {
		logger.info("****************Clean  Cache Begin,Cache Size:{}**********************",getCacheSize());
		contentCache.invalidateAll();
		contentCache.cleanUp();
		logger.info("****************Clean  Cache End,Cache Size:{}**********************",getCacheSize());
	}
	
	
}
