/**
 * 
 */
package com.udcm.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Raul	
 * 2017年5月23日
 */
public class SessionManager {
	
	private static final int TOKEN_TIME_OUT = 30*60*1000;//30分钟

	//Session池
	public static final Map<String,UserSession> SESSION_REPOSITORY = new HashMap<String,UserSession>();
	
	/**
	 * 通过token获取当前会话
	 * @param token
	 * @return
	 */
	public static UserSession getCurrentUser(String token){
		return SESSION_REPOSITORY.get(token);
	}
	
	/**
	 * 添加Session到会话池
	 * @param user
	 */
	public static void addSessionUser(String token,UserSession user){
		SESSION_REPOSITORY.put(token, user);
	}
	
	/**
	 * 将会话从Session池中移除
	 * @param token
	 */
	public static void removeSessionUser(String token){
		SESSION_REPOSITORY.remove(token);
	}
	
	
	//定时清理超时或残留的Session.
	static{		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Date now = new Date();				
				Set<Entry<String, UserSession>> entrys = SESSION_REPOSITORY.entrySet();
				if (null == entrys || entrys.isEmpty()) {
					return;
				}
				Iterator<Entry<String, UserSession>> it = entrys.iterator();
				if (null == it) {
					return;
				}
				while (it.hasNext()) {
					Entry<String, UserSession> entry  = it.next();					
					UserSession user  = entry.getValue();
					if(null==user) {
                        continue;
                    }
					if( now.getTime() > (user.getLastActiveTime().getTime() + TOKEN_TIME_OUT)){ //会话已经过期
						it.remove();
//						SESSION_REPOSITORY.remove(key);//将会话从池中剔除，需要重新登录
					}					
				}
				
			}
		}, 0,1*60*1000);//一分钟检查一次		
	}
}
