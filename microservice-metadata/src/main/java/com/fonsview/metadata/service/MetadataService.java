/**
 * 
 */
package com.fonsview.metadata.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 
 * @author hoob
 * @date 2018年8月3日上午11:49:42
 */
public interface MetadataService {
	/**
	 * @Title getChannelList
	 * @Description 获取频道列表
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getChannelList(HttpServletRequest request,String cpId,String type,String filter);
	/**
	 * @Title getSchedulList
	 * @Description 获取节目单列表
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	public List<Map<String, Object>> getSchedulList(String channelContentId,String starTime, String endTime) ;
	
	/**
	 * @Title getCategorysByContentId
	 * @Description 通过contentId获取栏目列表
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String, Object>> getCategorysByContentId(String contentId,String cpId); 
	
	/**
	 * @Title getCategorydetail
	 * @Description 获取栏目详情
	 * @param 
	 * @return Map<String,Object>
	 * @throws 
	 */
	public Map<String, Object> getCategorydetail(String categoryContentId);
	
	
	/**
	 * @Title getMediaByCategory
	 * @Description 获取栏目内容
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getMediaByCategoryWithSeries(HttpServletRequest request,String sortsql,String filtersql,String contentId, int pageSize,int begin);
	
	public int getMediaByCategoryWithSeriesCount(String sortsql,String filtersql,String contentId);

	/**
	 * @Title getMediaByCategory
	 * @Description 获取栏目内容
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getMediaByCategoryWithChannel(HttpServletRequest request,String sortsql,String filtersql,String contentId, int pageSize,int begin);
	public int getMediaByCategoryWithChannelCount(String sortsql,String filtersql,String contentId);

	/**
	 * @Title getLabels
	 * @Description 获取标签
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getLabels(String type);
	
	
	/**
	 * @Title getVODplayUrl
	 * @Description 获取点播播放地址
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getVODPlayUrl(HttpServletRequest request,String scontentId,String mContentIds,String clientIp,String userId);
	
	/**
	 * @Title getChannelPlayUrl
	 * @Description 获取直播的播放地址
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getChannelPlayUrl(HttpServletRequest request,String contentIds,String clientIp,String userId);

	
	/**
	 * @Title getFilters
	 * @Description 获取筛选条件
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getFilters(String type);
	
	/**
	 * @Title getSpecialByCategory
	 * @Description 获取指定栏目下的专题
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getSpecialByCategory(String categoryContentId);
	
	
	/**
	 * @Title getMediasBycontentId
	 * @Description 根据contentId批量获取内容
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getMediasBycontentId(String contentIds);
	
	
	/**
	 * @Title getMediaDetailBycontentId
	 * @Description 获取内容详情
	 * @param 
	 * @return Map<String,Object>
	 * @throws 
	 */
	public Map<String,Object>getMediaDetailBycontentId(String contentId,String code);

}
