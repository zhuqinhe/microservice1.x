/**
 * 
 */
package com.reminder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reminder.constants.ConfigKey;
import com.reminder.constants.Constants;
import com.reminder.dao.ReminderDao;
import com.reminder.model.Reminder;
import com.reminder.utils.DateUtils;
import com.reminder.utils.ThreadPoolUtil;
import com.reminder.utils.Utils;


/**
 * 提醒服务
 * <p>Title: RemindServiceImpl</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月4日
 */
@Service("reminderService")
public class RemindServiceImpl implements ReminderService{
	
	@Autowired
	private ReminderDao reminderDao;
	
	
	/**  
	 * 添加提醒  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月4日   
	 * @param reminder
	 * @return  
	 */ 
	@Override
	public Reminder addReminder(Reminder reminder) {
		reminder.setCreateTime(DateUtils.getCurrentTime());
		reminder.setUpdateTime(DateUtils.getCurrentTime());
		//判断用户提醒数量是否超标（默认用户最多可提交1个直播提醒，5个追剧提醒）
		List<Reminder> list = reminderDao.getReminder(reminder);
		String vodSize ="5";
		String liveSize ="1";
		if ((Constants.VOD.equals(reminder.getMediaType()) && list.size() >= Utils.obj2Int(vodSize))
		  ||(Constants.LIVE.equals(reminder.getMediaType()) && list.size() >= Utils.obj2Int(liveSize))){
			//删除最早设置的提醒
			removeEarliestRemind(reminder);
		}
		
		//添加提醒记录
		return reminderDao.addReminder(reminder);
	}


	/**  
	 * 删除提醒  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年1月7日   
	 * @param contentId
	 * @param userId
	 * @param mediaType  
	 */ 
	@Override
	public void removeReminder(String contentId, String userId, String mediaType) {
		
		 reminderDao.removeReminder(contentId,userId,mediaType);
	}


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
	@Override
	public List<Reminder> getReminderList(String userId, String contentId, String mediaType) {
		
		return reminderDao.getReminderList(userId, contentId, mediaType);
	}


	
	/**
	 * 删除最早的提醒
	 * <p>Title: removeEarliestRemind</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年2月27日   
	 * @param reminder
	 */
	private void removeEarliestRemind(Reminder reminder) {
		ThreadPoolUtil.createTask(new Runnable() {
			@Override
			public void run() {
				reminderDao.removeEarliestRemind(reminder);
			}
		});
	}
}
