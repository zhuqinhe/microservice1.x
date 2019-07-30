/**
 * 
 */
package com.udcm.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * <p>用户评分实体类</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月28日  
 */
@Document(collection = "score_collection")
public class Score {
	@Override
	public String toString() {
		return "Score [id=" + id + ", userId=" + userId + ", contentId=" + contentId + ", score=" + score
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	private String id;
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;		//用户id
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;	//剧头/合集内容ID
	private Integer score;		//用户评分，值选项[1-10]
	private String createTime;	//创建时间
	@Indexed(name="updateTime", unique=false, dropDups=false)
	private String updateTime;	//修改时间
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
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
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
