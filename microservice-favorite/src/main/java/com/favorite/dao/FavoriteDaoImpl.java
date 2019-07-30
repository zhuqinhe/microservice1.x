/**
 * 
 */
package com.favorite.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.favorite.constants.Constants;
import com.favorite.model.Favorite;
import com.favorite.utils.DateUtils;
import com.favorite.utils.StringUtils;


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
	 * @Title add
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public Favorite add(Favorite favorite) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(favorite.getUserId());
		criteria.and("contentId").is(favorite.getContentId());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("createTime",DateUtils.getCurrentTime());
		update.set("name", favorite.getName());
		update.set("thumbnailUrl", favorite.getThumbnailUrl());
		update.set("updateTime",DateUtils.getCurrentTime());
		update.set("mediaType",favorite.getMediaType());
		update.set("reserved",favorite.getReserved());
		update.set("subName",favorite.getSubName());
		update.set("cornerMark",favorite.getCornerMark());
		update.set("programType",favorite.getProgramType());
		update.set("source", favorite.getSource());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), Favorite.class,Constants.MONGO_FAVORITE_COL_NAME);
		
	}

	/**
	 * @Title deleteFavoriteByFavoriteSize
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public List<Favorite> deleteFavoriteByFavoriteSize(String userId, String size,String mediaType) {
		int int_size=Integer.parseInt(size);
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		if(StringUtils.isNotEmpty(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		List<Favorite>favorites=mongoTemplate.find(query,Favorite.class,Constants.MONGO_FAVORITE_COL_NAME);
		List<Favorite>deleteList=new ArrayList<Favorite>();
		if(favorites!=null&&favorites.size()>int_size){
			List<String>contentIds=new ArrayList<String>();
			for(int i=int_size;i<favorites.size();i++){
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
			mongoTemplate.remove(query, Constants.MONGO_FAVORITE_COL_NAME);
			
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
	@SuppressWarnings("deprecation")
	@Override
	public List<Favorite> getFavoriteList(String userId, Integer begin,
			Integer pageSize, String sp, String date,String mediaType) {
		         //查询条件
				Criteria criteria = new Criteria();
				criteria.and("userId").is(userId);
				criteria.and("updateTime").gt(date);
				if(StringUtils.isNotEmpty(mediaType)&&!"_all_".equals(mediaType)){
					criteria.and("mediaType").is(mediaType);
				}
				Query query = new Query(criteria);
				query.with(new Sort(new Order(Direction.DESC,"updateTime")));
				query.skip(begin);
				query.limit(pageSize);
				
				return mongoTemplate.find(query, Favorite.class, Constants.MONGO_FAVORITE_COL_NAME);
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
		criteria.and("userId").is(userId);
		if(StringUtils.isNotEmpty(mediaType)&&!"_all_".equals(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		return mongoTemplate.count(query, Constants.MONGO_FAVORITE_COL_NAME);
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
		return (List<Favorite>) mongoTemplate.findAllAndRemove(query,Favorite.class, Constants.MONGO_FAVORITE_COL_NAME);
		
	}

	/**
	 * @Title checkFavorite
	 * @Description 
	 * @param 
	 * @return FavoriteDao
	 * @throws 
	 */
	@Override
	public boolean checkFavorite(String userId, String contentId,String mediaType) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		criteria.and("contentId").is(contentId);
		if(StringUtils.isNotEmpty(mediaType)&&!"_all_".equals(mediaType)){
			criteria.and("mediaType").is(mediaType);
		}
		Query query = new Query(criteria);
		return mongoTemplate.exists(query,Constants.MONGO_FAVORITE_COL_NAME);
	}

	@Override
	public List<Favorite> getFavorites(String userId, String stbid) {
		
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		
		Query query = new Query(criteria);
		
		return mongoTemplate.find(query, Favorite.class, Constants.MONGO_FAVORITE_COL_NAME);
	}
}
