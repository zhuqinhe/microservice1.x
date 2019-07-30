/**
 * 
 */
package com.udcm.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.udcm.common.Constants;
import com.udcm.model.Favorite;
import com.udcm.utils.StringUtils;


/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午08:12:11
 */
@Repository("favoriteDao")
public class FavoriteDaoImpl implements FavoriteDao{
	@Resource
    private MongoTemplate mongoTemplate;

	
	/**
	 * @Title deleteFavoriteByFavoriteSize
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public List<Favorite> deleteFavoriteByFavoriteSize(String userId, String size,String mediaType) {
		int intSize=Integer.parseInt(size);
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		if(StringUtils.isNotEmpty(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<Favorite>favorites=mongoTemplate.find(query,Favorite.class,Constants.FAVORITE_COL_NAME);
		List<Favorite>deleteList=new ArrayList<Favorite>();
		if(favorites!=null&&favorites.size()>intSize){
			List<String>contentIds=new ArrayList<String>();
			for(int i=intSize;i<favorites.size();i++){
				contentIds.add(favorites.get(i).getContentId());
				deleteList.add(favorites.get(i));
			}
			criteria = new Criteria();
			criteria.and("userId").is(userId);
			criteria.and("contentId").in(contentIds);
			if(StringUtils.isNotEmpty(mediaType)){
				criteria.and("mediaType").is(mediaType);
			}
			query = new Query(criteria);
			mongoTemplate.remove(query, Constants.FAVORITE_COL_NAME);
			
		}
		return deleteList;
		
		

	}

	/**
	 * @Title getFavoriteList
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public List<Favorite> getFavoriteList(String userId, Integer begin,Integer pageSize) {
		         //查询条件
				Criteria criteria = new Criteria();
				if(StringUtils.isNotEmpty(userId)){
					criteria.and("userId").is(userId);
				}
				Query query = new Query(criteria);
				query.with(new Sort(new Order(Direction.DESC,"updateTime")));
				query.skip(begin);
				query.limit(pageSize);
				return mongoTemplate.find(query, Favorite.class, Constants.FAVORITE_COL_NAME);
	}

	/**
	 * @Title getFavoriteCount
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public long getFavoriteCount(String userId, String mediaType) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		if(StringUtils.isNotEmpty(mediaType)&&!"_all_".equals(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		return mongoTemplate.count(query, Constants.FAVORITE_COL_NAME);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public List<Favorite> remove(String userId, String contentIds, String mediaType) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		String[] contenids=contentIds.split(",");
		if(contenids!=null&&contenids.length>1){
			 criteria.and("contentId").in(Arrays.asList(contenids));
		}else{
			//等于1 时  要判断是否为_all_
			if("_all_".equals(contenids[0])){
				//删除所有
			}else{
				if(StringUtils.isNotEmpty(contenids[0])){
					criteria.and("contentId").is(contenids[0]);
				}
			}
		}
		if(StringUtils.isNotEmpty(mediaType)&&!"_all_".equals(mediaType)){
		 criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		return (List<Favorite>) mongoTemplate.findAllAndRemove(query,Favorite.class, Constants.FAVORITE_COL_NAME);
		
	}

	/* (non-Javadoc)
	 * @see com.fonsview.udc.dao.FavoriteDao#getFavorites(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Favorite> getFavorites(String userId, String stbid) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, Favorite.class, Constants.FAVORITE_COL_NAME);
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
	@Override
	public void removeAllNotInclude(String userId, List<String> saveList) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("mediaType").is("2");
		criteria.and("contentId").nin(saveList);
		
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Favorite.class, Constants.FAVORITE_COL_NAME);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		Criteria criteria = new Criteria();
		criteria.and("id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Favorite.class, Constants.FAVORITE_COL_NAME);
	}
}
