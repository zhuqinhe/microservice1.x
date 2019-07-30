/**
 * 
 */
package com.udcm.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description
 * @author hoob
 * @date 2018年11月28日下午1:58:47
 */
@Document(collection = "mediadata_collection")
public class MediaData {
	private String id;
	@Indexed(name = "contentId", unique = false, dropDups = false)
	private String contentId;
	private String thumbnailUrl;	//缩略图
	private String seriesFlag;		//单剧集还是多剧集  0:单集， 1多集
	private String programType;		//物理分类
	private String mediaType;		//媒资类型	2：点播，3：直播	
	private String totalNumber;		//总集数
	private String nowNumber;		//当前更新集数

	public String getTotalNumber() {
		return totalNumber;
	}

	public String getNowNumber() {
		return nowNumber;
	}

	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	public void setNowNumber(String nowNumber) {
		this.nowNumber = nowNumber;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getSeriesFlag() {
		return seriesFlag;
	}

	public void setSeriesFlag(String seriesFlag) {
		this.seriesFlag = seriesFlag;
	}

	public String getId() {
		return id;
	}

	public String getContentId() {
		return contentId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

}
