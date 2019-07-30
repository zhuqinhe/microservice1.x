/**
 * 
 */
package com.udcm.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "needtodelete_collection")
public class NeedToDelete implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;			//用户id
	private Integer times;			//添加次数
	private String groupType;		//分组
	private String createTime;		//创建时间
	@Indexed(direction=IndexDirection.DESCENDING,name="updateTime", unique=false, dropDups=false)
	private String updateTime;		//更新时间
	
	private Boolean synchronize;		//是否同步
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
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
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
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public Boolean getSynchronize() {
		return synchronize;
	}
	public void setSynchronize(Boolean synchronize) {
		this.synchronize = synchronize;
	}
	
}
