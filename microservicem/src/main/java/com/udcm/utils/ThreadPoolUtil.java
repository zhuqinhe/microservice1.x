/**
 * 
 */
package com.udcm.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
/**
 * @Description 
 * @author hoob
 * @date 2019年1月28日上午11:13:47
 */
public class ThreadPoolUtil {
	private static final  ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(100,
			new BasicThreadFactory.Builder().namingPattern("thread-pool-util-%d").daemon(true).build()); 
	/*public static void createFavoriteTask(Favorite favorite){
		fixedThreadPool.execute(new  Runnable() {
			@Override
			public void run() {
				
			}});
	}*/
	private static final  ScheduledExecutorService deteleService = new ScheduledThreadPoolExecutor(3,
			new BasicThreadFactory.Builder().namingPattern("thread-pool-util-%d").daemon(true).build()); 
	public static void createTask(Runnable runnable){
		executorService.execute(runnable);
	}
	public static void deleteTask(Runnable runnable){
		deteleService.execute(runnable);
	}
}
