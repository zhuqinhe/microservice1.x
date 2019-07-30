/**
 * 
 */
package com.fonsview.metadata.dao;

import java.util.List;
import java.util.Map;
/**
 * @Description 
 * @author hoob
 * @date 2018年8月3日上午11:47:17
 */
public interface MetadataDao {
/**
 * @Title getChannelList
 * @Description 获取频道列表
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getChannelList(String cpId,String type,String filter,String language);
/**
 * @Title getFilterList
 * @Description 获取筛选条件
 * @param 
 * @return List<Filter>
 * @throws 
 */
public List<Map<String,Object>>getFilterList(String codes,String language);

/**
 * @Title getSchedulList
 * @Description 获取节目单列表
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getSchedulList(String channelContentId,String starTime,String endTime);

/**
 * @Title getCategorysByContentId
 * @Description 获取栏目列表(父栏目下的子栏目)
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getCategorysByContentId(String contentId ,String cpId);

/**
 * @Title getFilterByCategory
 * @Description 获取栏目下的筛选条件
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>> getFilterByCategory(String categoryContentId);

/**
 * @Title getSortByCategory
 * @Description 获取栏目下的排序
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>> getSortByCategory(String categoryContentId);


/**
 * @Title getCategorydetail
 * @Description 获取栏目详情
 * @param 
 * @return Map<String,Object>
 * @throws 
 */
public Map<String,Object>getCategorydetail(String categoryContentId);


/**
 * @Title getMediaByCategory
 * @Description 获取栏目内容
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getMediaByCategorySeries(String sortsql,String filtersql,String identityno,String language, int pageSize,int begin);

public int getMediaByCategorySeriesCount(String sortsql,String filtersql,String identityno,String language);


/**
 * @Title getMediaByCategoryChannel
 * @Description 获取栏目下的直播内容
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getMediaByCategoryChannel(String sortsql,String filtersql,String identityno,String language ,int pageSize,int begin);

public int getMediaByCategoryChannelCount(String sortsql,String filtersql,String identityno,String language);


/**
 * @Title getMVodPlayURL
 * @Description 通过movie的contentId获取播放地址
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getMVodPlayURL(String mcontentIs);
/**
 * @Title getSVodPlayURL
 * @Description 通过合集contentid获取播放地址
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getSVodPlayURL(String scontentId);
/**
 * @Title getChannlePlayURL
 * @Description 获取直播的播放地址
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getChannlePlayURL(String scontentId);
/**
 * @Title getLabels
 * @Description 获取标签
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getLabels(String type);

/**
 * @Title getFilters
 * @Description 获取筛选条件
 * @param 
 * @return List<Map<String,Object>>
 * @throws 
 */
public List<Map<String,Object>>getFilters(String type);

/**
 * @Title getSortList
 * @Description 获取排序
 * @param 
 * @return List<Sort>
 * @throws 
 */
public List<Map<String,Object>> getSortList(String codes,String language);


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
 * @Description 根据contentId批量获取媒体内容
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
