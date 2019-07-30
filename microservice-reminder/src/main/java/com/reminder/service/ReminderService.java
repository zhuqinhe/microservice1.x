/**
 * 
 */
package com.reminder.service;

import java.util.List;

import com.reminder.model.Reminder;


/**
 * 提醒服务
 * <p>Title: RemindService</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月4日
 */
public interface ReminderService {
	
	/**
	 * 添加提醒
	 * <p>Title: addReminder</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param reminder
	 * @return
	 */
	Reminder addReminder(Reminder reminder);

	/**
	 * 删除提醒
	 * <p>Title: removeReminder</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月7日   
	 * @param contentId
	 * @param userId
	 * @param mediaType
	 * @return
	 */
	void removeReminder(String contentId, String userId, String mediaType);

	/**
	 * 获取用户提醒列表
	 * <p>Title: getReminderList</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月8日   
	 * @param userId
	 * @param contentId
	 * @param mediaType
	 * @return
	 */
	List<Reminder> getReminderList(String userId, String contentId, String mediaType);

	
}
