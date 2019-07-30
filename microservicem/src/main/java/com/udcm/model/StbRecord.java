/**
 * 
 */
package com.udcm.model;

import java.io.Serializable;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月14日下午8:02:06
 */
public class StbRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userId;
	private String stbId;
	private String createTime;
	private String updateTime;
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getStbId() {
		return stbId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}

}
