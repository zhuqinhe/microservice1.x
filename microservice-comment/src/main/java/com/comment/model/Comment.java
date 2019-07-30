/**
 * 
 */
package com.comment.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * <p>用户评论实体类</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2018年12月28日  
 */
@Document(collection = "comment_collection")
public class Comment {
	private String id;
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;				//用户id
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;			//剧头/合集内容ID
	private String comment;				//评论内容
	private String commentParentId;		//被评论的Id(回复内容评论的需要必填;评论内容本身不需要)
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentParentId() {
		return commentParentId;
	}
	public void setCommentParentId(String commentParentId) {
		this.commentParentId = commentParentId;
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
