/**
 * 
 */
package com.udcm.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.udcm.common.ConfigKey;
import com.udcm.common.Constants;
import com.udcm.dao.FavoriteDao;
import com.udcm.model.Favorite;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.StringUtils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午10:20:15
 */
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {
	private Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);

	@Resource
	private FavoriteDao favoriteDao;
	//@Resource 
//	private KafkaService kafkaService;
	//@Resource
	//private MqService mqService;
	
	/**
	 * @throws ParseException 
	 * @Title getFavoriteList
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public List<Favorite> getFavoriteList(String userId, Integer begin,
			Integer pageSize) throws Exception {
		//是否拉取芒果的最新记录  逻辑调整，以他们的记录为准，
		List<Favorite>result=new ArrayList<Favorite>();
		//直播的收藏记录 不从咪咕获取
		//如果begin和pageSize为空，设置默认值
		begin = StringUtils.obj2Int(begin,Constants.BEGIN);
		pageSize= StringUtils.obj2Int(pageSize,Constants.PAGESIZE);
		String favoriteSize=ConfigUtil.getProperties(ConfigKey.FAVORITESIZE.name());
		if(StringUtils.isNotEmpty(favoriteSize)){
			int booksize=Integer.parseInt(favoriteSize);
		}
	    result= favoriteDao.getFavoriteList(userId, begin, pageSize);
		return result;
	}

	
	/**
	 * 删除 saveList 之外的所有记录
	 * <p>Title: removeAllNotInclude</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年2月13日   
	 * @param userId
	 * @param saveList
	 */
	private void removeAllNotInclude(String userId, List<String> saveList) {
		
		favoriteDao.removeAllNotInclude(userId, saveList);
	}
	
	
	/**
	 * @Title getFavoriteCount
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public long getFavoriteCount(String userId, String mediaType) {
		//获取用收藏时先拉取
		return favoriteDao.getFavoriteCount(userId, mediaType);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public void remove(String userId, String contentIds, String mediaType,String groupType,boolean synchronize) {
	    List<Favorite>deleteList=favoriteDao.remove(userId, contentIds, mediaType);
	    
	    if(synchronize&&"mango".equals(ConfigUtil.getProperties(ConfigKey.SCENE.name()))&&deleteList!=null&&!deleteList.isEmpty()){
	    	for(Favorite favorite:deleteList){
	    		//mqService.sendFavoriteMessage(favorite, Constants.ACTION_DELETE);
	    		//kafkaService.sendFavoriteMessage(favorite,Constants.ACTION_DELETE);
	    	}
	    }
	}
	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		// TODO Auto-generated method stub
		favoriteDao.remove(ids);
	}

}
