/**
 * 
 */
package com.udcm.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 提醒实体类
 * <p>Title: Reminder</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年1月4日
 */
@Document(collection = "reminder_collection")
public class Reminder {
	private String id;
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;				//用户id
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;			//剧头/合集内容ID/直播ID
	private String name;				//剧头/直播名称
	private String mediaType;			//媒体类型：2：点播；3：直播
	private String reminderStrategy;	//提醒策略  暂定（1：直播前5分钟；2：直播前十分钟），追剧暂用不上
	private String currentEpisodes;		//点播追剧时，当前看到第几集
	private String createTime;			//创建时间
	@Indexed(name="updateTime", unique=false, dropDups=false)
	private String updateTime;			//修改时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getReminderStrategy() {
		return reminderStrategy;
	}
	public void setReminderStrategy(String reminderStrategy) {
		this.reminderStrategy = reminderStrategy;
	}
	public String getCurrentEpisodes() {
		return currentEpisodes;
	}
	public void setCurrentEpisodes(String currentEpisodes) {
		this.currentEpisodes = currentEpisodes;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
