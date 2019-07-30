/**
 * 
 */
package com.favorite.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午07:44:59
 */
@Document(collection = "favorite_collection")
/*@CompoundIndexes(
        {
                @CompoundIndex(name = "userId_contentId",def = "{'userId':-1,'contentId':1}")
        })*/
public class Favorite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;//contentId  剧头的contentId
	private String name;//名称
	private String thumbnailUrl;//缩略图
	private String mediaType;//媒体类型
	private String reserved;//预留字段
	private String createTime;//创建时间
	private String subName;//子名称
	private String cornerMark;//角标
	private String programType;//物理分类
	@Indexed(direction=IndexDirection.DESCENDING,name="updateTime", unique=false, dropDups=false)
	private String updateTime;
	private String source;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getName() {
		return name;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public String getMediaType() {
		return mediaType;
	}
	public String getReserved() {
		return reserved;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getSubName() {
		return subName;
	}
	public String getCornerMark() {
		return cornerMark;
	}
	public String getProgramType() {
		return programType;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public void setCornerMark(String cornerMark) {
		this.cornerMark = cornerMark;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
