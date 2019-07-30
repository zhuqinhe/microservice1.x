/**
 * 
 */
package com.fonsview.sync.service;

import java.util.List;
import java.util.Map;

/**
 * @Description 
 * @author hoob
 * @date 2018年10月29日下午5:34:17
 */
public interface CmsDataSyncService {
	public Map<String,Object> prcessCmsData(String msgType,String startTime,String endTime,int start,int num,Map<String,Object>rsponse);
	public Map<String,Object> prcessCmsDataForDass(String msgType,String startTime,String endTime,int start,int num,Map<String,Object>rsponse);
	public List<Map<String,Object>>getCmsDataForUDC(String startTime,String endTime,int begin,int pageSize,int mediaType);

}
