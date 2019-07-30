/**
 * 
 */
package com.comment.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * 内容评分
 * <p>Title: ContentScore</p>  
 * <p>Description: 用于存放每个内容的平均分，定时刷新</p>  
 * @author Graves  
 * @date 2019年1月9日  
 */
@Document(collection = "contentscore_collection")
public class ContentScore {
	@Indexed(name="contentId", unique=false, dropDups=false)
	private String contentId;		//剧头/合集内容ID
	private Integer totalScore;		//总分
	private Integer totalNum;		//记录总数
	private String createTime;		//创建时间
	@Indexed(name="updateTime", unique=false, dropDups=false)
	private String updateTime;		//修改时间
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
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
	 * @param totalScore
	 * @param totalNum
	 * @param createTime
	 * @param updateTime
	 */
	public ContentScore(String contentId, Integer totalScore, Integer totalNum, String createTime, String updateTime) {
		super();
		this.contentId = contentId;
		this.totalScore = totalScore;
		this.totalNum = totalNum;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	
}
