/**
 * 
 */
package com.reminder.dao;

import java.util.List;

import com.reminder.model.Reminder;



/**
 * 提醒
 * <p>Title: ReminderDao</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月4日
 */
public interface ReminderDao {

	/**
	 * 添加提醒
	 * <p>Title: addRemind</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param reminder
	 * @return
	 */
	Reminder addReminder(Reminder reminder);

	/**  
	 * 查询用户提醒
	 * <p>Title: getRemind</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param reminder
	 * @return  
	 */ 
	List<Reminder> getReminder(Reminder reminder);

	/**
	 * 删除最早设置的提醒
	 * <p>Title: removeEarliestRemind</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param reminder
	 */
	void removeEarliestRemind(Reminder reminder);

	/**  
	 * 删除提醒  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月7日   
	 * @param contentId
	 * @param userId
	 * @param mediaType  
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
