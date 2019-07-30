package com.udcm.vo;

import java.util.List;

/**
 * @Description 
 * @author hoob
 * @date 2019年5月29日上午11:53:50
 */
public class BatchRequest{
	private List<String>ids;
	private List<String> contentIds;
	private Integer mediaType;
	public List<String> getIds() {
		return ids;
	}
	public List<String> getContentIds() {
		return contentIds;
	}
	public Integer getMediaType() {
		return mediaType;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public void setContentIds(List<String> contentIds) {
		this.contentIds = contentIds;
	}
	public void setMediaType(Integer mediaType) {
		this.mediaType = mediaType;
	}   
	
}
