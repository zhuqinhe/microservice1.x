/**
 * 
 */
package com.favorite.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.favorite.constants.ConfigKey;
import com.favorite.constants.Constants;
import com.favorite.dao.FavoriteDao;
import com.favorite.model.Favorite;
import com.favorite.utils.DateUtils;
import com.favorite.utils.DateUtils.Pattern;
import com.favorite.utils.StringUtils;
import com.favorite.utils.ThreadPoolUtil;
import com.favorite.utils.Utils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午10:20:15
 */
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

	@Resource
	private FavoriteDao favoriteDao;
	/**
	 * @Title add
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public void add(Favorite favorite,boolean  synchronize,String groupType) {
		favorite.setCreateTime(DateUtils.getCurrentTime());
		favorite.setUpdateTime(favorite.getCreateTime());
		favorite.setUserId(favorite.getUserId());
		//根据业务处理
		favorite=favoriteDao.add(favorite);
	
	}
	private void deleteFavoriteByFavoriteSize(Favorite favorite,boolean synchronize, String groupType) {
		ThreadPoolUtil.createTask(new  Runnable() {
			@Override
			public void run() {
		    //检查是否有数量限制，有则需要淘汰久的
			//后期改用配置
			List<Favorite>deleteList=favoriteDao.deleteFavoriteByFavoriteSize(favorite.getUserId(),"20",favorite.getMediaType());
			}});
	}
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
			Integer pageSize, String sp, String date,String mediatype,String groupType,boolean synchronize) throws Exception {
		List<Favorite>result=new ArrayList<Favorite>();
		//如果begin和pageSize为空，设置默认值
		begin = Utils.obj2Int(begin,Constants.BEGIN);
		pageSize= Utils.obj2Int(pageSize,Constants.PAGESIZE);
		//如果date 为空，则设置默认值为三个月前
		if (Utils.objIsNull(date)) {
			String limitmonth=null;//ConfigUtil.getProperties(ConfigKey.LIMITMONTH.name());
			if(StringUtils.isEmpty(limitmonth)){
				limitmonth="3";
			}
			date = DateUtils.plusMonth(-(Integer.parseInt(limitmonth)), DateUtils.getCurrentTime(), Pattern.yyyy_MM_dd.getValue());
		}
	    result= favoriteDao.getFavoriteList(userId, begin, pageSize, sp,date,mediatype);
		return result;
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
	   	}

	/**
	 * @Title checkFavorite
	 * @Description 
	 * @param 
	 * @return FavoriteService
	 * @throws 
	 */
	@Override
	public boolean checkFavorite(String userId, String contentId,String mediatype,String groupType,boolean synchronize) {

		return favoriteDao.checkFavorite(userId, contentId,mediatype);
		
	}

}
