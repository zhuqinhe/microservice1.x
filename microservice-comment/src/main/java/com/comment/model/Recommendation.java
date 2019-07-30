/**
 * 
 */
package com.comment.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * <p>用户踩/赞 实体类</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月28日  
 */
@Document(collection = "recommendation_collection")
public class Recommendation {
	private String id;	
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;			//用户id
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;		//剧头/合集内容ID
	private String name;
	
	private String choice;			//是否推荐  值选项：A：赞  V：踩
	private String createTime;		//创建时间
	@Indexed(name="updateTime", unique=false, dropDups=false)
	private String updateTime;		//修改时间
	private Integer recycle = 0;	//0  未取消；1  已取消
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
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

	public Integer getRecycle() {
		return recycle;
	}

	public void setRecycle(Integer recycle) {
		this.recycle = recycle;
	}
	
}
