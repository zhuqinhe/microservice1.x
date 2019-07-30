/**
 * 
 */
package com.fonsview.metadata.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fonsview.metadata.config.Config;
import com.fonsview.metadata.dao.MetadataDao;
import com.fonsview.metadata.utils.CacheUtils;
import com.fonsview.metadata.utils.HttpUtils;
import com.fonsview.metadata.utils.MapUtils;
import com.fonsview.metadata.utils.StringUtils;
import com.fonsview.metadata.utils.Utils;

/**
 * @Description 
 * @author hoob
 * @date 2018年8月3日上午11:50:01
 */
@Service("metadataService")
public class MetadataServiceImpl implements MetadataService{
	private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	@Resource
	private MetadataDao metadataDao;
    @Autowired
    private Config config;
	/**
	 * @Title getChannelList
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getChannelList(HttpServletRequest request,String cpId, String type,String filter) {
		//处理赛选条件
		
		List<Map<String,Object>>channls=metadataDao.getChannelList(cpId, type, filter,config.getDefault_language());
		//处理图片地址
		if(channls!=null&&channls.size()>0){
			if(StringUtils.isNotEmpty(config.getPicture_url_prefix())){
				for(Map<String,Object>map:channls){
					if(map!=null){
						if(map.get("thumbnailURL")!=null&&StringUtils.isNotEmpty(map.get("thumbnailURL").toString())){
							map.put("thumbnailURL", config.getPicture_url_prefix()+"/"+map.get("thumbnailURL"));
						}
						if(map.get("posterURL")!=null&&StringUtils.isNotEmpty(map.get("posterURL").toString())){
							map.put("posterURL", config.getPicture_url_prefix()+"/"+map.get("posterURL"));
						}
					}
				}
			}
		}
		//处理播放地址,时移地址
	/*	if(channls!=null&&channls.size()>0){
			for(Map<String,Object>map:channls){
				try {
					if(map!=null&&map.get("playURL")!=null){
						map.put("playURL", Utils.processJsonUrl(request,map.get("playURL").toString()));
						map.put("playURL",Utils.processUrlEncryptKey(request,map.get("playURL").toString(),map.get("contentId").toString()));
						map.put("multicastURL", map.get("playURL"));
					}
					if(map!=null&&map.get("timeshiftUrl")!=null){
						map.put("timeshiftURL", Utils.processJsonUrl(request,map.get("timeshiftURL").toString()));
						map.put("timeshiftURL",Utils.processUrlEncryptKey(request,map.get("timeshiftURL").toString(),map.get("contentId").toString()));
					}
				} catch (Exception e) {
					logger.error(" url is error");
				}
			}
		}*/
		return channls;
	}

	/**
	 * @Title getSchedulList
	 * @Description 获取节目单
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getSchedulList(String channelContentId,
			String date, String days) {
		int days_int=1;
		if(StringUtils.isNotEmpty(days)){
			days_int=StringUtils.handleIntParam(days);
		}
		//获取向前推进的days天日期
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			if (StringUtils.isEmpty(date)) {
				Date temp = new Date();
				cal.setTime(temp);
				date = smf.format(temp);
			} else {
				cal.setTime(smf.parse(date));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		cal.add(Calendar.DAY_OF_YEAR, -(days_int - 1));
		if(StringUtils.isNotEmpty(channelContentId)){
			if(channelContentId.startsWith(",")){
				channelContentId=channelContentId.substring(1,channelContentId.length());
			}
			if(channelContentId.endsWith(",")){
				channelContentId=channelContentId.substring(0,channelContentId.length()-1);
			}
		}

		List<Map<String, Object>> scheduleList = metadataDao.getSchedulList(channelContentId, smf.format(cal.getTime()), date);


		SimpleDateFormat smf3 = new SimpleDateFormat("yyyyMMddHHmmss");
		long currentTime = System.currentTimeMillis();
		if(scheduleList!=null&&scheduleList.size()>0){
			try {
				
				for (Map<String, Object> map : scheduleList) {
					long startL = smf3.parse(
							(String) map.get("date") + map.get("startTime")).getTime();
					long endtL = smf3.parse(
							(String) map.get("date") + map.get("endTime")).getTime();
					// recordStatus节目单录制状态，0：未录制，1：录制中，2、录制成功，3：录制失败
					if (currentTime < startL){
						map.put("recordStatus", 0);
					} else if (currentTime > endtL) {
						map.put("recordStatus", 2);
					} else {
						map.put("recordStatus", 1);
					}

					StringBuilder dateS = new StringBuilder(
							(String) map.get("date"));
					if (dateS.indexOf("-") < 0) {
						dateS.insert(6, "-").insert(4, "-");
						map.put("date", dateS.toString());
					}
					StringBuilder startTimeS = new StringBuilder(
							(String) map.get("startTime"));
					if (startTimeS.indexOf(":") < 0) {
						startTimeS.delete(4, startTimeS.length()).insert(2, ":");
						map.put("startTime", startTimeS.toString());
					}
					StringBuilder endTimeS = new StringBuilder(
							(String) map.get("endTime"));
					if (endTimeS.indexOf(":") < 0) {
						endTimeS.delete(4, endTimeS.length()).insert(2, ":");
						map.put("endTime", endTimeS.toString());
					}
					if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("posterUrl")!=null&&StringUtils.isNotEmpty(map.get("posterUrl").toString())){
						map.put("posterUrl", config.getPicture_url_prefix()+"/"+map.get("posterUrl"));
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return scheduleList;
	}

	/**
	 * @Title getCategorysByContentId
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getCategorysByContentId(String contentId,String cpId) {
		List<Map<String,Object>>categorys=metadataDao.getCategorysByContentId(contentId,cpId);
		if(categorys!=null&&categorys.size()>0){
			//处理图片
			
			for(Map<String,Object> category:categorys){
				//获取栏目上关联排序
				List<Map<String,Object>>sortList=metadataDao.getSortByCategory(category.get("contentId").toString());//判空检查
				List<Map<String,Object>>newSort=new ArrayList<Map<String,Object>>();
				if(sortList!=null&&sortList.size()>0){
					for(Map<String,Object> s:sortList){
						Map<String,Object>tmpSort=new HashMap<String,Object>();
						tmpSort.put("code",s.get("code"));
						tmpSort.put("name",s.get("name"));
						newSort.add(tmpSort);
					}
				}
				category.put("sorts",newSort);
				//获取栏目上的filter
				List<Map<String,Object>>filters=metadataDao.getFilterByCategory(category.get("contentId").toString());
				if(filters!=null&&filters.size()>0){
					category.put("filters", filters);
				}
				if(category.get("subCategoryNumber")!=null&&Integer.parseInt(category.get("subCategoryNumber").toString())>0){
					category.put("isLeaf", 0);//非叶子节点
				}else{
					category.put("isLeaf", 1);//叶子节点
				}

				if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&category.get("bgImageURL")!=null){
					category.put("bgImageURL",config.getPicture_url_prefix()+"/"+category.get("bgImageURL"));
				}
				if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&category.get("thumbnailURL")!=null){
					category.put("thumbnailURL",config.getPicture_url_prefix()+"/"+category.get("thumbnailURL"));
				}
			}
		}
		return categorys;
	}

	/**
	 * @Title getCategorydetail
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public Map<String, Object> getCategorydetail(String categoryContentId) {
		Map<String,Object>category=metadataDao.getCategorydetail(categoryContentId);
		if(category==null||category.isEmpty()){
			return null;
		}
		
		List<Map<String,Object>>sortList=metadataDao.getSortByCategory(category.get("contentId").toString());//判空检查
		List<Map<String,Object>>newSort=new ArrayList<Map<String,Object>>();
		if(sortList!=null&&sortList.size()>0){
			for(Map<String,Object> s:sortList){
				Map<String,Object>tmpSort=new HashMap<String,Object>();
				tmpSort.put("code",s.get("code"));
				tmpSort.put("name",s.get("name"));
				newSort.add(tmpSort);
			}
		}
		category.put("sorts",newSort);
		//获取栏目上的filter
		List<Map<String,Object>>filters=metadataDao.getFilterByCategory(category.get("contentId").toString());
		category.put("filters", filters);
		
		if(category.get("subCategoryNumber")!=null&&Integer.parseInt(category.get("subCategoryNumber").toString())>0){
			category.put("isLeaf", 0);//非叶子节点
		}else{
			category.put("isLeaf", 1);//叶子节点
		}
		if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&category.get("bgImageURL")!=null){
			category.put("bgImageURL",config.getPicture_url_prefix()+"/"+category.get("bgImageURL"));
		}
		if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&category.get("thumbnailURL")!=null){
			category.put("thumbnailURL",config.getPicture_url_prefix()+"/"+category.get("thumbnailURL"));
		}
		return category;
	}

	/**
	 * @Title getMediaByCategory
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediaByCategoryWithSeries(HttpServletRequest request,String sortsql,
			String filtersql, String contentId, int pageSize, int begin) {
	
		//处理赛选条件
		String identityno=MapUtils.IDENTITYNOORCONTENTID.get(contentId);
		if(StringUtils.isEmpty(identityno)){
			Map<String,Object>category=metadataDao.getCategorydetail(contentId);
			if(category!=null&&!category.isEmpty()){
				identityno=category.get("identityno")==null?"":category.get("identityno").toString();
				if(StringUtils.isNotEmpty(identityno)){
					MapUtils.IDENTITYNOORCONTENTID.put(contentId, identityno);
					MapUtils.IDENTITYNOORCONTENTID.put(identityno, contentId);
				}
			}
		}
		if(StringUtils.isEmpty(identityno)){
			return null;
		}
		List<Map<String,Object>>result=metadataDao.getMediaByCategorySeries(sortsql,filtersql, identityno,config.getPicture_url_prefix(), pageSize, begin);
		//处理播放地址，图片
		if(result!=null&&!result.isEmpty()){
			for(Map<String,Object>map:result){
				if(map!=null&&StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("thumbnailUrl")!=null&&StringUtils.isNotEmpty(map.get("thumbnailUrl").toString())){
					map.put("thumbnailURL", config.getPicture_url_prefix()+"/"+map.get("thumbnailUrl"));
				}
				if(map!=null&&StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("posterUrl")!=null&&StringUtils.isNotEmpty(map.get("posterUrl").toString())){
					map.put("posterURL", config.getPicture_url_prefix()+"/"+map.get("posterUrl"));
				}
				/*if(map!=null&&map.get("playUrl")!=null){
					try {
						map.put("playUrl", Utils.processJsonUrl(request,map.get("playUrl").toString()));
						map.put("playUrl",Utils.processUrlEncryptKey(request,map.get("playUrl").toString(),map.get("contentId").toString()));
					} catch (Exception e) {
						
						logger.error("playUrl is error, contentId="+map.get("contentId"));;
					}
				}*/
			}
		}
		return result;
	}
	public int getMediaByCategoryWithSeriesCount(String sortsql,String filtersql, String contentId) {
		
		int result=0;
		//处理赛选条件
		String identityno=MapUtils.IDENTITYNOORCONTENTID.get(contentId);
		if(StringUtils.isEmpty(identityno)){
			Map<String,Object>category=metadataDao.getCategorydetail(contentId);
			if(category!=null&&!category.isEmpty()){
				identityno=category.get("identityno")==null?"":category.get("identityno").toString();
				if(StringUtils.isNotEmpty(identityno)){
					MapUtils.IDENTITYNOORCONTENTID.put(contentId, identityno);
					MapUtils.IDENTITYNOORCONTENTID.put(identityno, contentId);
				}
			}
		}
		if(StringUtils.isEmpty(identityno)){
			return result;
		}
		result=metadataDao.getMediaByCategorySeriesCount(sortsql, filtersql, identityno,config.getPicture_url_prefix());
		
		return result;
	}
	/**
	 * @Title getMediaByCategoryWithChannel
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediaByCategoryWithChannel(HttpServletRequest request,
			String sortsql, String filtersql, String contentId, int pageSize,
			int begin) {
	
		//处理赛选条件
		String identityno=MapUtils.IDENTITYNOORCONTENTID.get(contentId);
		if(StringUtils.isEmpty(identityno)){
			Map<String,Object>category=metadataDao.getCategorydetail(contentId);
			if(category!=null&&!category.isEmpty()){
				identityno=category.get("identityno")==null?"":category.get("identityno").toString();
				if(StringUtils.isNotEmpty(identityno)){
					MapUtils.IDENTITYNOORCONTENTID.put(contentId, identityno);
					MapUtils.IDENTITYNOORCONTENTID.put(identityno, contentId);
				}
			}
		}
		if(StringUtils.isEmpty(identityno)){
			return null;
		}
		List<Map<String,Object>>result=metadataDao.getMediaByCategoryChannel(sortsql, filtersql, identityno,config.getPicture_url_prefix(), pageSize, begin);
		//处理播放地址，图片
				if(result!=null&&!result.isEmpty()){
					for(Map<String,Object>map:result){
						if(map!=null&&StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("thumbnailURL")!=null&&StringUtils.isNotEmpty(map.get("thumbnailURL").toString())){
							map.put("thumbnailURL", config.getPicture_url_prefix()+"/"+map.get("thumbnailURL"));
						}
						if(map!=null&&StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("posterURL")!=null&&StringUtils.isNotEmpty(map.get("posterURL").toString())){
							map.put("posterURL", config.getPicture_url_prefix()+"/"+map.get("posterURL"));
						}
						try {
							if(map!=null&&map.get("playUrl")!=null){
								map.put("playUrl", Utils.processJsonUrl(request,map.get("playUrl").toString(),config));
								map.put("playUrl",Utils.processUrlEncryptKey(request,map.get("playUrl").toString(),map.get("contentId").toString(),config));
								map.put("multicastUrl", map.get("playUrl"));
							}
							if(map!=null&&map.get("timeshiftUrl")!=null){
								map.put("timeshiftUrl", Utils.processJsonUrl(request,map.get("timeshiftUrl").toString(),config));
								map.put("timeshiftUrl",Utils.processUrlEncryptKey(request,map.get("timeshiftUrl").toString(),map.get("contentId").toString(),config));
							}
						} catch (Exception e) {
							logger.error("playUrl is error, contentId="+map.get("contentId"));;
						}
					}
				}
		return result;
	}
	
	
	public int  getMediaByCategoryWithChannelCount(String sortsql, String filtersql, String contentId) {
		int result=0;
		//处理赛选条件
		String identityno=MapUtils.IDENTITYNOORCONTENTID.get(contentId);
		if(StringUtils.isEmpty(identityno)){
			Map<String,Object>category=metadataDao.getCategorydetail(contentId);
			if(category!=null&&!category.isEmpty()){
				identityno=category.get("identityno")==null?"":category.get("identityno").toString();
				if(StringUtils.isNotEmpty(identityno)){
					MapUtils.IDENTITYNOORCONTENTID.put(contentId, identityno);
					MapUtils.IDENTITYNOORCONTENTID.put(identityno, contentId);
				}
			}
		}
		if(StringUtils.isEmpty(identityno)){
			return result;
		}
		result=metadataDao.getMediaByCategoryChannelCount(sortsql, filtersql, identityno,config.getPicture_url_prefix());
		
	
		return result;
	}
	/**
	 * @Title getLabels
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getLabels(String type) {
		List<Map<String,Object>>result=metadataDao.getLabels(type);
		if(result!=null&&!result.isEmpty()){
		
			for(Map<String,Object>map:result){
				if(map!=null&&StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("imageURL")!=null&&StringUtils.isNotEmpty(map.get("imageURL").toString())){
					map.put("imageURL", config.getPicture_url_prefix()+"/"+map.get("imageURL"));
				}
			}
		}
		return result;
	}

	/**
	 * @Title getVODplayUrl
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getVODPlayUrl(HttpServletRequest request,String scontentId,String mContentIds,String clientIp,String userId) {
		if(StringUtils.isNotEmpty(mContentIds)){
			if(mContentIds.startsWith(",")){
				mContentIds=mContentIds.substring(1,mContentIds.length());
			}
			if(mContentIds.endsWith(",")){
				mContentIds=mContentIds.substring(0,mContentIds.length()-1);
			}
		}
		List<Map<String,Object>>result=null;
		String key=String.format("metadata_getVODPlayUrl(%s)", mContentIds);
		Object obj=CacheUtils.getCacheData(key);
		if(obj!=null){
			result=(List<Map<String, Object>>) obj;
		}else{
			result=metadataDao.getMVodPlayURL(mContentIds);
			if(result==null||result.isEmpty()){
				result=metadataDao.getSVodPlayURL(scontentId);
			}
		}
		if(StringUtils.isEmpty(clientIp)){
			clientIp=HttpUtils.getIpAddr(request);
		}
		if(result!=null&&!result.isEmpty()){
			for(Map<String,Object>map:result){
				if(map!=null&&map.get("playURL")!=null){
					try {
					    map.put("playURL", Utils.processJsonUrl(clientIp,map.get("playURL").toString(),config));
						map.put("playURL",Utils.processUrlEncryptKey(request,map.get("playURL").toString() ,map.get("mContentId").toString(),config));
					} catch (Exception e) {
						logger.info("playUrl is error ,scontentId="+scontentId+",mcontentIds="+mContentIds);
					}
				}
			}
		}
		return result;
	}
	/**
	 * @Title getChannelPlayUrl
	 * @Description 获取直播的播放地址
	 * @param 
	 * @return MetadataV1ServiceImpl
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChannelPlayUrl(HttpServletRequest request,String contentIds,String clientIp,String userId) {
		if(StringUtils.isNotEmpty(contentIds)){
			if(contentIds.startsWith(",")){
				contentIds=contentIds.substring(1,contentIds.length());
			}
			if(contentIds.endsWith(",")){
				contentIds=contentIds.substring(0,contentIds.length()-1);
			}
		}
		List<Map<String,Object>>result=null;
		String key=String.format("metadata_getChannelPlayUrl(%s)", contentIds);
		Object obj=CacheUtils.getCacheData(key);
		if(obj!=null){
			result=(List<Map<String, Object>>) obj;
		}else{
			result=metadataDao.getChannlePlayURL(contentIds);
		}
		if(StringUtils.isEmpty(clientIp)){
			clientIp=HttpUtils.getIpAddr(request);
		}
		if(result!=null&&!result.isEmpty()){
			for(Map<String,Object>map:result){
				if(map!=null&&map.get("playURL")!=null){
					try {
					    map.put("playURL", Utils.processJsonUrl(clientIp,map.get("playURL").toString(),config));
					    map.put("timeshiftURL", Utils.processJsonUrl(clientIp,map.get("timeshiftURL").toString(),config));
						map.put("playURL",Utils.processUrlEncryptKey( request,map.get("playURL").toString(), map.get("contentId").toString(),config));
						map.put("timeshiftURL",Utils.processUrlEncryptKey( request,map.get("timeshiftURL").toString() , map.get("contentId").toString(),config));

					} catch (Exception e) {
						logger.info("playUrl is error ,contentIds="+contentIds);
					}
				}
			}
		}
		return result;
	}
	/**
	 * @Title getFilters
	 * @Description 根据code获取筛选条件
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getFilters(String type) {
		
		return metadataDao.getFilters(type);
	}

	/**
	 * @Title getSpecialByCategory
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getSpecialByCategory(String categoryContentId) {
		List<Map<String,Object>>result=this.metadataDao.getSpecialByCategory(categoryContentId);
		if(result!=null&&!result.isEmpty()){
			
			for(Map<String,Object>map:result){
				if(map!=null&&!map.isEmpty()){
					if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("bgImageURL")!=null){
						map.put("bgImageURL",config.getPicture_url_prefix()+"/"+map.get("bgImageURL"));
					}
					if(StringUtils.isNotEmpty(config.getPicture_url_prefix())&&map.get("thumbnailURL")!=null){
						map.put("thumbnailURL",config.getPicture_url_prefix()+"/"+map.get("thumbnailURL"));
					}
				}
			}
		}
		return result;
	}

	/**
	 * @Title getMediasBycontentId
	 * @Description 根据contentId批量获取内容
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediasBycontentId(String contentIds) {
		if(StringUtils.isNotEmpty(contentIds)){
			if(contentIds.startsWith(",")){
				contentIds=contentIds.substring(1,contentIds.length());
			}
			if(contentIds.endsWith(",")){
				contentIds=contentIds.substring(0,contentIds.length()-1);
			}
		}
		List<Map<String,Object>>result=this.metadataDao.getMediasBycontentId(contentIds);
		if(result!=null&&!result.isEmpty()){
			if(StringUtils.isNotEmpty(config.getPicture_url_prefix())){
				for(Map<String,Object>map:result){
					if(map!=null&&!map.isEmpty()){
						if(map.get("posterURl")!=null&&StringUtils.isNotEmpty(map.get("posterUrl").toString())){
							map.put("posterUrl", config.getPicture_url_prefix()+"/"+map.get("posterUrl"));
						}
						if(map.get("thumbnailUrl")!=null&&StringUtils.isNotEmpty(map.get("thumbnailUrl").toString())){
							map.put("thumbnailUrl", config.getPicture_url_prefix()+"/"+map.get("thumbnailUrl"));
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * @Title getMediaDetailBycontentId
	 * @Description 
	 * @param 
	 * @return MetadataV1Service
	 * @throws 
	 */
	@Override
	public Map<String, Object> getMediaDetailBycontentId(String contentId,String code) {
		
		return this.metadataDao.getMediaDetailBycontentId(contentId, code);
	}

}
