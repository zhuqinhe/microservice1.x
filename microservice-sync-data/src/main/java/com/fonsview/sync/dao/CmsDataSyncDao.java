/**
 * 
 */
package com.fonsview.sync.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description 
 * @author hoob
 * @date 2018年10月29日下午6:52:31
 */
public interface CmsDataSyncDao {
	public List<Map<String,Object>>prcessCmsData310(String msgType,String startTime,String endTime,int start,int num,String language);
	public List<String>getCategoryContentIds310(String contentId,int mediaType);
	public List<Map<String,Object>>prcessCmsData300(String msgType,String startTime,String endTime,int start,int num,String language);
	public List<String>getCategoryContentIds300(String contentId,int mediaType);
	public List<Map<String,Object>>prcessCmsData310ForDass(String msgType,String startTime,String endTime,int start,int num,String language);
	public List<Map<String,Object>>prcessCmsData300ForDass(String msgType,String startTime,String endTime,int start,int num,String language);
	public String getProgramContentId(String movieConentId,String version);
	public String getSeriesContentId(String movieConentId,String version);
	public List<Map<String, Object>> getCmsDataForUDC300(String startTime,String endTime, int begin, int pageSize, int mediaType);
	public List<Map<String, Object>> getCmsDataForUDC310(String startTime,String endTime, int begin, int pageSize, int mediaType);
}
