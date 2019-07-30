/**
 * 
 */
package com.fonsview.sync.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fonsview.sync.common.ConfigKey;
import com.fonsview.sync.config.Config;
import com.fonsview.sync.dao.CmsDataSyncDao;
import com.fonsview.sync.utils.StringUtils;
import com.fonsview.sync.utils.Utils;


/**
 * @Description 
 * @author hoob
 * @date 2018年10月29日下午5:26:30
 */
@Service("cmsDataSyncService")
public class CmsDataSyncServiceImpl implements CmsDataSyncService{
	@Resource
	private CmsDataSyncDao cmsDataSyncDao;
	@Autowired
	private Config config;
	/**
	 * @Title prcessCmsData
	 * @Description 
	 * @param 
	 * @return CmsDataSyncService
	 * @throws 
	 */
	@Override
public Map<String,Object> prcessCmsData(String msgType,String startTime,String endTime,int start,int num,Map<String,Object>rsponse){
		String version=config.getCmsversion();
		//获取当前配置的cms版本号
		if(StringUtils.isEmpty(version)||"300".equals(version)){
			return processCmsData300(msgType, startTime, endTime, start, num,
					rsponse);
		}else{
			return processCmsData310(msgType, startTime, endTime, start, num,
					rsponse);
		}
		
	}
	private Map<String, Object> processCmsData310(String msgType,
			String startTime, String endTime, int start, int num,
			Map<String, Object> rsponse) {
		List<Map<String,Object>>maps=cmsDataSyncDao.prcessCmsData310(msgType, startTime, endTime, start, num,config.getDefault_language());
		if(maps!=null&&!maps.isEmpty()){
			//处理结果集
			if("GetUpdatedCategory".equals(msgType)){
			   for(Map<String,Object>map:maps){
				   if(map!=null&&!map.isEmpty()){
					  if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
						  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
						  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
					  }
					  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
						  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
					  }else{
						  map.put("updateTime",map.get("createTime"));
					  }
					  if(map.get("Picture")!=null&&StringUtils.isNotEmpty(map.get("Picture").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
						  map.put("Picture",config.getPicture_url_prefix()+"/"+map.get("Picture").toString());
					  }
					  if(map.get("Model")!=null){
						  if("8".equals(map.get("Model"))){
							  map.put("ModelName","混合栏目"); 
						  }
						  else if("0".equals(map.get("Model"))){
							  map.put("ModelName","混合栏目"); 
						  }
						  else if("1".equals(map.get("Model"))){
							  map.put("ModelName","普通专题"); 
						  }
						  else if("2".equals(map.get("Model"))){
							  map.put("ModelName","电视精选"); 
						  }
						  else if("3".equals(map.get("Model"))){
							  map.put("ModelName","点播分集"); 
						  } 
						  else if("4".equals(map.get("Model"))){
							  map.put("ModelName","点播排行榜"); 
						  }
						  else if("5".equals(map.get("Model"))){
							  map.put("ModelName","直播"); 
						  }
						  else if("6".equals(map.get("Model"))){
							  map.put("ModelName","应用"); 
						  }
						  else if("7".equals(map.get("Model"))){
							  map.put("ModelName","互动专题"); 
						  }
						  else if("8".equals(map.get("Model"))){
							  map.put("ModelName","人物"); 
						  }
					  }
				   }
				   
			   }
			   rsponse.put("TotalNum", maps.size());
			   rsponse.put("Categorys", maps);
			   return rsponse;
			}
			else if("GetDeletedCategory".equals(msgType)){
                   List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("CategoryIds", list);
				   return rsponse;
			}else if("GetUpdatedSeries".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds310(map.get("contentId").toString(), 2);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Series", map);
						   m2.put("CategoryIds", categoryIds);
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("ReleaseYear")!=null){
							  map.put("ReleaseYear",map.get("ReleaseYear").toString()+"-01-01 00:00:00"+ ".000");
						  }
						  if(map.get("DetailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("DetailPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("DetailPicUrl",config.getPicture_url_prefix()+"/"+map.get("DetailPicUrl").toString());
						  }
						  if(map.get("PosterPicUrl")!=null&&StringUtils.isNotEmpty(map.get("PosterPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("PosterPicUrl",config+"/"+map.get("PosterPicUrl").toString());
						  }
						  if(map.get("Status")!=null){
							  map.put("Status", map.get("Status").toString().equals("1")?12:8);
						  }else{
							  map.put("Status",8); 
						  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("SeriesInfos",list2);
				   return rsponse;
			}else if("GetDeletedSeries".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("SeriesIds", list);
				   return rsponse;
			}else if("GetUpdatedProgram".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds310(map.get("contentId").toString(), 1);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Program", map);
						   m2.put("CategoryIds", categoryIds);
						   m2.put("SeriesId", map.get("SeriesId"));
						   m2.put("seriesContentId",map.get("SeriesId"));
						   map.remove("SeriesId");
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("ReleaseYear")!=null){
							  map.put("ReleaseYear",map.get("ReleaseYear").toString()+"-01-01 00:00:00"+ ".000");
						  }
						  if(map.get("DetailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("DetailPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("DetailPicUrl",config+"/"+map.get("DetailPicUrl").toString());
						  }
						  if(map.get("PosterPicUrl")!=null&&StringUtils.isNotEmpty(map.get("PosterPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("PosterPicUrl",config+"/"+map.get("PosterPicUrl").toString());
						  }
						  if(map.get("Status")!=null){
							  map.put("Status", map.get("Status").toString().equals("1")?12:8);
						  }else{
							  map.put("Status",8); 
						  }
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ProgramInfos",list2);
				   return rsponse;
			}else if("GetDeletedProgram".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ProgramIds", list);
				   return rsponse;
			}else if("GetUpdatedMedia".equals(msgType)){

				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Media", map);
						   m2.put("ProgramId", map.get("ProgramId"));
						   m2.put("programContentId", map.get("ProgramId"));
						   map.remove("ProgramId");
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("Status")!=null&&StringUtils.isNotEmpty(map.get("Status").toString())){
							  map.put("Status", map.get("Status").toString().equals("1")?12:8);
						  }else{
							  map.put("Status",8);
						  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("MediaInfos", list2);
				   return rsponse;
			}else if("GetDeletedMedia".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("MediaIds", list);
				   return rsponse;
			}else if("GetUpdatedChannel".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds310(map.get("contentId").toString(), 1);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Channel", map);
						   m2.put("CategoryIds", categoryIds);
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("Poster")!=null&&StringUtils.isNotEmpty(map.get("Poster").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("Poster",config.getPicture_url_prefix()+"/"+map.get("Poster").toString());
						  }
						
						  if(map.get("Status")!=null&&StringUtils.isNotEmpty(map.get("Status").toString())){
							  map.put("Status", map.get("Status").toString().equals("1")?12:8);
						  }else{
							  map.put("Status",8);
						  }
						  map.put("BitRateType", 1);//随便取值，暂时没用上
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ChannelInfos",list2);
				   return rsponse;
			}else if("GetUpdatedSchedule".equals(msgType)){

				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("PicUrl")!=null&&StringUtils.isNotEmpty(map.get("PicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							  map.put("PicUrl",config.getPicture_url_prefix()+"/"+map.get("PicUrl").toString());
						  }
						  if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("StartTime")!=null&&StringUtils.isNotEmpty(map.get("StartTime").toString())){
							  map.put("startTime",map.get("StartTime").toString());
							  map.put("StartTime", map.get("date").toString().substring(0,4)+"-"+map.get("date").toString().substring(5,6)+"-"+map.get("date").toString().substring(7,8)+" "+
									  map.get("StartTime").toString().substring(0,2)+":"+ map.get("StartTime").toString().substring(3,4)+":"+ map.get("StartTime").toString().substring(5,6)+".000");
						  }
						  if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("EndTime")!=null&&StringUtils.isNotEmpty(map.get("EndTime").toString())){
							  map.put("endTime",map.get("EndTime").toString());
							  map.put("EndTime", map.get("date").toString().substring(0,4)+"-"+map.get("date").toString().substring(5,6)+"-"+map.get("date").toString().substring(7,8)+" "+
									  map.get("EndTime").toString().substring(0,2)+":"+ map.get("EndTime").toString().substring(3,4)+":"+ map.get("EndTime").toString().substring(5,6)+".000");
						  }
					   }
				   }
				   rsponse.put("TotalNum", maps.size());
				   rsponse.put("Schedules", maps);
				   return rsponse;
			}else if("GetDeletedSchedule".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ScheduleIds", list);
				   return rsponse;
			}else if("GetDeleteChannel".equals(msgType)){
				List<String>list=new ArrayList<String>();
				for(Map<String,Object>map:maps){
					list.add(map.get("contentId").toString());
				}
				rsponse.put("TotalNum", list.size());
				rsponse.put("ContentIds", list);
				return rsponse;
						}
		}
		return rsponse;
	}
	private Map<String,Object> processCmsData300(String msgType,
			String startTime, String endTime, int start, int num,
			Map<String, Object> rsponse) {
		List<Map<String,Object>>maps=cmsDataSyncDao.prcessCmsData300(msgType, startTime, endTime, start, num,config.getDefault_language());
		if(maps!=null&&!maps.isEmpty()){
			//处理结果集
			String PICTURE_URL_PREFIX_OLD=config.getPicture_url_prefix_old();
			String PICTURE_URL_PREFIX_NEW=config.getPicture_url_prefix_new();
			if("GetUpdatedCategory".equals(msgType)){
			   for(Map<String,Object>map:maps){
				   if(map!=null&&!map.isEmpty()){
					   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
							  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
							  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
						  }
						  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
							  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
						  }else{
							  map.put("updateTime",map.get("createTime"));
						  }
					  if(map.get("Picture")!=null&&StringUtils.isNotEmpty(map.get("Picture").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
						  map.put("Picture",map.get("Picture").toString().replace(PICTURE_URL_PREFIX_OLD, PICTURE_URL_PREFIX_NEW));
					  }
					  if(map.get("Model")!=null){
						  if("8".equals(map.get("Model"))){
							  map.put("ModelName","混合栏目"); 
						  }
						  else if("0".equals(map.get("Model"))){
							  map.put("ModelName","混合栏目"); 
						  }
						  else if("1".equals(map.get("Model"))){
							  map.put("ModelName","普通专题"); 
						  }
						  else if("2".equals(map.get("Model"))){
							  map.put("ModelName","电视精选"); 
						  }
						  else if("3".equals(map.get("Model"))){
							  map.put("ModelName","点播分集"); 
						  } 
						  else if("4".equals(map.get("Model"))){
							  map.put("ModelName","点播排行榜"); 
						  }
						  else if("5".equals(map.get("Model"))){
							  map.put("ModelName","直播"); 
						  }
						  else if("6".equals(map.get("Model"))){
							  map.put("ModelName","应用"); 
						  }
						  else if("7".equals(map.get("Model"))){
							  map.put("ModelName","互动专题"); 
						  }
						  else if("8".equals(map.get("Model"))){
							  map.put("ModelName","人物"); 
						  }
					  }
				   }
				   
			   }
			   rsponse.put("TotalNum", maps.size());
			   rsponse.put("Categorys", maps);
			   return rsponse;
			}
			else if("GetDeletedCategory".equals(msgType)){
                   List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("CategoryIds", list);
				   return rsponse;
			}else if("GetUpdatedSeries".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds300(map.get("contentId").toString(), 2);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Series", map);
						   m2.put("CategoryIds", categoryIds);
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("ReleaseYear")!=null){
							  map.put("ReleaseYear",map.get("ReleaseYear").toString()+"-01-01 00:00:00"+ ".000");
						  }
						  if(map.get("DetailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("DetailPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("DetailPicUrl",map.get("DetailPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD, PICTURE_URL_PREFIX_NEW));
						  }
						  if(map.get("PosterPicUrl")!=null&&StringUtils.isNotEmpty(map.get("PosterPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("PosterPicUrl",map.get("PosterPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD,PICTURE_URL_PREFIX_NEW));
						  }
						  if(map.get("Status")!=null){
							  map.put("Status", map.get("Status").toString().equals("12")?12:8);
						  }else{
							  map.put("Status",8); 
						  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("SeriesInfos",list2);
				   return rsponse;
			}else if("GetDeletedSeries".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("SeriesIds", list);
				   return rsponse;
			}else if("GetUpdatedProgram".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds300(map.get("contentId").toString(), 1);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Program", map);
						   m2.put("CategoryIds", categoryIds);
						   m2.put("SeriesId", map.get("SeriesId"));
						   m2.put("seriesContentId",map.get("SeriesId"));
						   map.remove("SeriesId");
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("ReleaseYear")!=null){
							  map.put("ReleaseYear",map.get("ReleaseYear").toString()+"-01-01 00:00:00"+ ".000");
						  }
						  if(map.get("DetailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("DetailPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("DetailPicUrl",map.get("DetailPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD,PICTURE_URL_PREFIX_NEW));
						  }
						  if(map.get("PosterPicUrl")!=null&&StringUtils.isNotEmpty(map.get("PosterPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("PosterPicUrl",map.get("PosterPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD,PICTURE_URL_PREFIX_NEW));
						  }
						  if(map.get("Status")!=null){
							  map.put("Status", map.get("Status").toString().equals("12")?12:8);
						  }else{
							  map.put("Status",8); 
						  }
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ProgramInfos",list2);
				   return rsponse;
			}else if("GetDeletedProgram".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ProgramIds", list);
				   return rsponse;
			}else if("GetUpdatedMedia".equals(msgType)){

				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Media", map);
						   m2.put("ProgramId", map.get("ProgramId"));
						   m2.put("programContentId",map.get("ProgramId"));
						   map.remove("ProgramId");
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("Status")!=null&&StringUtils.isNotEmpty(map.get("Status").toString())){
							  map.put("Status", map.get("Status").toString().equals("12")?12:8);
						  }else{
							  map.put("Status",8);
						  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("MediaInfos", list2);
				   return rsponse;
			}else if("GetDeletedMedia".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("MediaIds", list);
				   return rsponse;
			}else if("GetUpdatedChannel".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   List<String> categoryIds=cmsDataSyncDao.getCategoryContentIds300(map.get("contentId").toString(), 1);
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Channel", map);
						   m2.put("CategoryIds", categoryIds);
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("Poster")!=null&&StringUtils.isNotEmpty(map.get("Poster").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("Poster",map.get("Poster").toString().replace(PICTURE_URL_PREFIX_OLD,PICTURE_URL_PREFIX_NEW));
						  }
						
						  if(map.get("Status")!=null&&StringUtils.isNotEmpty(map.get("Status").toString())){
							  map.put("Status", map.get("Status").toString().equals("12")?12:8);
						  }else{
							  map.put("Status",8);
						  }
						  map.put("BitRateType", 1);//随便取值，暂时没用上
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ChannelInfos",list2);
				   return rsponse;
			}else if("GetUpdatedSchedule".equals(msgType)){

				   for(Map<String,Object>map:maps){
					  
					   if(map!=null&&!map.isEmpty()){
						   if(map.get("CreateDate")!=null&&StringUtils.isNotEmpty(map.get("CreateDate").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")));
								  map.put("CreateDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("CreateDate")) + ".000");
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("PublishTime")!=null&&StringUtils.isNotEmpty(map.get("PublishTime").toString())){
							  map.put("PublishTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("PublishTime")) + ".000");
						  }
						  if(map.get("ValidTime")!=null&&StringUtils.isNotEmpty(map.get("ValidTime").toString())){
							  map.put("ValidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("ValidTime")) + ".000");
						  }
						  if(map.get("InvalidTime")!=null&&StringUtils.isNotEmpty(map.get("InvalidTime").toString())){
							  map.put("InvalidTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("InvalidTime")) + ".000");
						  }
						  if(map.get("PicUrl")!=null&&StringUtils.isNotEmpty(map.get("PicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							  map.put("PicUrl",map.get("PicUrl").toString().replace(PICTURE_URL_PREFIX_OLD,PICTURE_URL_PREFIX_NEW));
						  }
						  if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("StartTime")!=null&&StringUtils.isNotEmpty(map.get("StartTime").toString())){
							  map.put("startTime", map.get("StartTime").toString().replace(":",""));
						  }
						  if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("EndTime")!=null&&StringUtils.isNotEmpty(map.get("EndTime").toString())){
							  map.put("endTime", map.get("EndTime").toString().replace(":",""));
						  }
						  if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())){
							  String t= map.get("date").toString();
							  map.put("date",t.replace("-",""));
						  }
					   }
				   }
				   rsponse.put("TotalNum", maps.size());
				   rsponse.put("Schedules", maps);
				   return rsponse;
			}else if("GetDeletedSchedule".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ScheduleIds", list);
				   return rsponse;
			}else if("GetDeleteChannel".equals(msgType)){
				List<String>list=new ArrayList<String>();
				for(Map<String,Object>map:maps){
					list.add(map.get("contentId").toString());
				}
				rsponse.put("TotalNum", list.size());
				rsponse.put("ContentIds", list);
				return rsponse;
						}
		}
		return rsponse;
	}
	/**
	 * @Title prcessCmsDataForDass
	 * @Description 
	 * @param 
	 * @return CmsDataSyncService
	 * @throws 
	 */
	@Override
	public Map<String, Object> prcessCmsDataForDass(String msgType,
			String startTime, String endTime, int start, int num,
			Map<String, Object> rsponse) {
		String version=config.getCmsversion();
		//获取当前配置的cms版本号
		if(StringUtils.isEmpty(version)||"300".equals(version)){
			return processCmsData300ForDass(msgType, startTime, endTime, start, num,
					rsponse);
		}else{
			return processCmsData310ForDass(msgType, startTime, endTime, start, num,
				rsponse);
		}
	}
	
	private Map<String, Object> processCmsData310ForDass(String msgType,
			String startTime, String endTime, int start, int num,
			Map<String, Object> rsponse) {
		List<Map<String,Object>>maps=cmsDataSyncDao.prcessCmsData310ForDass(msgType, startTime, endTime, start, num,config.getCmsversion());
		if(maps!=null&&!maps.isEmpty()){
			//处理结果集
			if("GetUpdatedCategory".equals(msgType)){
			   for(Map<String,Object>map:maps){
				   if(map!=null&&!map.isEmpty()){
					  if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
						  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
					  }
					  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
						  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
					  }else{
						  map.put("updateTime",map.get("createTime"));
					  }
				   }
				   
			   }
			   rsponse.put("TotalNum", maps.size());
			   rsponse.put("Categorys", maps);
			   return rsponse;
			}
			else if("GetDeletedCategory".equals(msgType)){
                   List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("CategoryIds", list);
				   return rsponse;
			}else if("GetUpdatedSeries".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Series", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
								
							  }
						  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						  if(map.get("status")!=null){
							  map.put("status", map.get("status").toString().equals("1")?12:8);
						  }else{
							  map.put("status",8); 
						  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("SeriesInfos",list2);
				   return rsponse;
			}else if("GetDeletedSeries".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("SeriesIds", list);
				   return rsponse;
			}else if("GetUpdatedProgram".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   String seriescontentId=cmsDataSyncDao.getSeriesContentId(map.get("contentId").toString(), "310");
						   m2.put("SeriesId", seriescontentId);
						   m2.put("Program", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						 
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ProgramInfos",list2);
				   return rsponse;
			}else if("GetDeletedProgram".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ProgramIds", list);
				   return rsponse;
			}else if("GetUpdatedMedia".equals(msgType)){

				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   String programContentId=cmsDataSyncDao.getProgramContentId(map.get("contentId").toString(),"310");
						   m2.put("ProgramId", programContentId);
						   list2.add (m2);
						   m2.put("Media", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("MediaInfos", list2);
				   return rsponse;
			}else if("GetDeletedMedia".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("MediaIds", list);
				   return rsponse;
			}else if("GetUpdatedChannel".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Channel", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ChannelInfos",list2);
				   return rsponse;
			}else if("GetUpdatedSchedule".equals(msgType)){

				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
				   }
				   rsponse.put("TotalNum", maps.size());
				   rsponse.put("Schedules", maps);
				   return rsponse;
			}else if("GetDeletedSchedule".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ScheduleIds", list);
				   return rsponse;
			}else if("GetDeleteChannel".equals(msgType)){
				List<String>list=new ArrayList<String>();
				for(Map<String,Object>map:maps){
					list.add(map.get("contentId").toString());
				}
				rsponse.put("TotalNum", list.size());
				rsponse.put("ContentIds", list);
				return rsponse;
						}
		}
		return rsponse;
	}
	private Map<String,Object> processCmsData300ForDass(String msgType,
			String startTime, String endTime, int start, int num,
			Map<String, Object> rsponse) {
		List<Map<String,Object>>maps=cmsDataSyncDao.prcessCmsData300ForDass(msgType, startTime, endTime, start, num,config.getDefault_language());
		if(maps!=null&&!maps.isEmpty()){
			//处理结果集
			if("GetUpdatedCategory".equals(msgType)){
			   for(Map<String,Object>map:maps){
				   if(map!=null&&!map.isEmpty()){
					  if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
						  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
					  }
					  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
						  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
					  }else{
						  map.put("updateTime",map.get("createTime"));
					  }
				   }
				   
			   }
			   rsponse.put("TotalNum", maps.size());
			   rsponse.put("Categorys", maps);
			   return rsponse;
			}
			else if("GetDeletedCategory".equals(msgType)){
                   List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("CategoryIds", list);
				   return rsponse;
			}else if("GetUpdatedSeries".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Series", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
								
							  }
						  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("SeriesInfos",list2);
				   return rsponse;
			}else if("GetDeletedSeries".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("SeriesIds", list);
				   return rsponse;
			}else if("GetUpdatedProgram".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   String seriescontentId=cmsDataSyncDao.getSeriesContentId(map.get("contentId").toString(), "300");
						   m2.put("SeriesId", seriescontentId);
						   list2.add (m2);
						   m2.put("Program", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
						 
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ProgramInfos",list2);
				   return rsponse;
			}else if("GetDeletedProgram".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ProgramIds", list);
				   return rsponse;
			}else if("GetUpdatedMedia".equals(msgType)){

				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   String programContentId=cmsDataSyncDao.getProgramContentId(map.get("contentId").toString(),"300");
						   m2.put("ProgramId", programContentId);
						   list2.add (m2);
						   m2.put("Media", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("MediaInfos", list2);
				   return rsponse;
			}else if("GetDeletedMedia".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("MediaIds", list);
				   return rsponse;
			}else if("GetUpdatedChannel".equals(msgType)){
				  List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				   for(Map<String,Object>map:maps){
					   if(map!=null&&!map.isEmpty()){
						   Map<String, Object> m2 = new HashMap<String, Object>();
						   list2.add (m2);
						   m2.put("Channel", map);
						   if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
								  map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
							  }
							  if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
								  map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
							  }else{
								  map.put("updateTime",map.get("createTime"));
							  }
					   }
					   
				   }
				   rsponse.put("TotalNum", list2.size());
				   rsponse.put("ChannelInfos",list2);
				   return rsponse;
			}else if("GetUpdatedSchedule".equals(msgType)){

				for(Map<String,Object>map:maps){
					if(map!=null&&!map.isEmpty()){
						if(map.get("createTime")!=null&&StringUtils.isNotEmpty(map.get("createTime").toString())){
							map.put("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("createTime")));
						}
						if(map.get("updateTime")!=null&&StringUtils.isNotEmpty(map.get("updateTime").toString())){
							map.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(map.get("updateTime")));
						}else{
							map.put("updateTime",map.get("createTime"));
						}
						if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("StartTime")!=null&&StringUtils.isNotEmpty(map.get("StartTime").toString())){
							map.put("startTime", map.get("StartTime").toString().replace(":",""));
						}
						if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())&&map.get("EndTime")!=null&&StringUtils.isNotEmpty(map.get("EndTime").toString())){
							map.put("endTime", map.get("EndTime").toString().replace(":",""));
						}
						if(map.get("date")!=null&&StringUtils.isNotEmpty(map.get("date").toString())){
							String t= map.get("date").toString();
							map.put("date",t.replace("-",""));
						}
					}
				}
				   rsponse.put("TotalNum", maps.size());
				   rsponse.put("Schedules", maps);
				   return rsponse;
			}else if("GetDeletedSchedule".equals(msgType)){
				 List<String>list=new ArrayList<String>();
				   for(Map<String,Object>map:maps){
					   list.add(map.get("contentId").toString());
				   }
				   rsponse.put("TotalNum", list.size());
				   rsponse.put("ScheduleIds", list);
				   return rsponse;
			}else if("GetDeleteChannel".equals(msgType)){
				List<String>list=new ArrayList<String>();
				for(Map<String,Object>map:maps){
					list.add(map.get("contentId").toString());
				}
				rsponse.put("TotalNum", list.size());
				rsponse.put("ContentIds", list);
				return rsponse;
						}
		}
		return rsponse;
	}
	/**
	 * @Title getCmsDataForUDC
	 * @Description 
	 * @param 
	 * @return CmsDataSyncService
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getCmsDataForUDC(String startTime,
			String endTime, int begin, int pageSize, int mediaType) {
		String version=config.getCmsversion();
		//图片地址替换;
		if(StringUtils.isEmpty(version)||"300".equals(version)){
			List<Map<String, Object>> list= cmsDataSyncDao.getCmsDataForUDC300(startTime, endTime, begin, pageSize, mediaType);
			if(list!=null&&list.size()>0){
				//处理结果集
				String PICTURE_URL_PREFIX_OLD=config.getPicture_url_prefix_old();
				String PICTURE_URL_PREFIX_NEW=config.getPicture_url_prefix_new();
				if(mediaType==2){
					for(Map<String,Object>map:list){
						if(map.get("detailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("detailPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							map.put("detailPicUrl",map.get("detailPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD, PICTURE_URL_PREFIX_NEW));
						}
						if("1000".equals(map.get("seriesFlag"))){
							map.put("seriesFlag",0);//普通vod
						}else{
							map.put("seriesFlag",1);//连续剧
						}
					}
				}else if(mediaType==3){
					for(Map<String,Object>map:list){
						if(map.get("logoUrl")!=null&&StringUtils.isNotEmpty(map.get("logoUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							map.put("logoUrl",map.get("logoUrl").toString().replace(PICTURE_URL_PREFIX_OLD, PICTURE_URL_PREFIX_NEW));
						}
					}

				}else if(mediaType==1){
					for(Map<String,Object>map:list){
						if(map.get("detailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("detailPicUrl").toString())&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_OLD)&&StringUtils.isNotEmpty(PICTURE_URL_PREFIX_NEW)){
							map.put("detailPicUrl",map.get("detailPicUrl").toString().replace(PICTURE_URL_PREFIX_OLD, PICTURE_URL_PREFIX_NEW));
						}
					}
				}
			}
			return list;
		}else{
			List<Map<String, Object>> list= cmsDataSyncDao.getCmsDataForUDC310(startTime, endTime, begin, pageSize, mediaType);
			if(list!=null&&list.size()>0){
				//处理结果集
				if(mediaType==2){
					for(Map<String,Object>map:list){
						if(map.get("detailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("detailPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							map.put("detailPicUrl",config.getPicture_url_prefix()+"/"+map.get("detailPicUrl").toString());
						}
					}
				}else if(mediaType==3){
					for(Map<String,Object>map:list){
						if(map.get("logoUrl")!=null&&StringUtils.isNotEmpty(map.get("logoUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							map.put("logoUrl",config.getPicture_url_prefix()+"/"+map.get("logoUrl").toString());
						}
					}

				}else if(mediaType==1){
					for(Map<String,Object>map:list){
						if(map.get("detailPicUrl")!=null&&StringUtils.isNotEmpty(map.get("detailPicUrl").toString())&&StringUtils.isNotEmpty(config.getPicture_url_prefix())){
							map.put("detailPicUrl",config.getPicture_url_prefix()+"/"+map.get("detailPicUrl").toString());
						}
					}
				}
			}
			return list;
		}
	}
}
