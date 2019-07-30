/**
 * 
 */
package com.bookmark.model;


import java.io.Serializable;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午08:04:58
 */
@Document(collection = "bookmark_collection")
public class Bookmark implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@Indexed(name="userId", unique=false, dropDups=false)
	private String userId;
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String	contentId;
	private String	name;	
	private String 	pContentId;
	private String 	episodeName;
	@Indexed(name="index", unique=false, dropDups=false)
	private int	index;
	private int	time;
	private int	length;
	private String	thumbnailUrl;
	private String	createTime;
	@Indexed(direction=IndexDirection.DESCENDING,name="updateTime", unique=false, dropDups=false)
	private String updateTime;
	private String programType;
	private String periods;
	@Indexed(name="mediaType", unique=false, dropDups=false)
	private String mediaType;
	private String source;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContentId() {
		return contentId;
	}
	public String getName() {
		return name;
	}
	public String getpContentId() {
		return pContentId;
	}
	public String getEpisodeName() {
		return episodeName;
	}
	public int getIndex() {
		return index;
	}
	public int getTime() {
		return time;
	}
	public int getLength() {
		return length;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getProgramType() {
		return programType;
	}
	public String getPeriods() {
		return periods;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setpContentId(String pContentId) {
		this.pContentId = pContentId;
	}
	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public void setPeriods(String periods) {
		this.periods = periods;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
