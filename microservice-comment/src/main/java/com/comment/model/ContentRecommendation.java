/**
 * 
 */
package com.comment.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * 内容踩/赞
 * <p>Title: ContentRecommendation</p>  
 * <p>Description: 用于存放内容的踩/赞总数，定时刷新</p>  
 * @author Graves  
 * @date 2019年1月9日  
 */
@Document(collection = "contentrecommendation_collection")
public class ContentRecommendation {
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;			//剧头/合集内容ID
	private Integer totalA;				//赞总数
	private Integer totalV;				//踩总数
	private String createTime;			//创建时间
	@Indexed(name="updateTime", unique=false, dropDups=false)
	private String updateTime;			//修改时间
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public Integer getTotalA() {
		return totalA;
	}
	public void setTotalA(Integer totalA) {
		this.totalA = totalA;
	}
	public Integer getTotalV() {
		return totalV;
	}
	public void setTotalV(Integer totalV) {
		this.totalV = totalV;
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
	/**
	 * @param contentId
	 * @param totalA
	 * @param totalV
	 * @param createTime
	 * @param updateTime
	 */
	public ContentRecommendation(String contentId, Integer totalA, Integer totalV, String createTime,
			String updateTime) {
		super();
		this.contentId = contentId;
		this.totalA = totalA;
		this.totalV = totalV;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
}
