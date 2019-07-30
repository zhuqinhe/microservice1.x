/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.Favorite;
/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午08:11:47
 */
public interface FavoriteDao{
public List<Favorite> deleteFavoriteByFavoriteSize(String userId,String size,String mediaType);
public List<Favorite> getFavoriteList(String userId, Integer begin, Integer pageSize);
public long getFavoriteCount(String userId,String mediaType);
public List<Favorite> remove(String userId,String contentIds,String mediaType);
/**  
 * <p>Title: getFavorites</p>  
 * <p>Description: </p>  
 * @author Graves
 * @date 2018年11月14日   
 * @param userId
 * @param stbid
 * @return  
 */ 
public List<Favorite> getFavorites(String userId, String stbid);
/**  
 * <p>Title: removeAllNotInclude</p>  
 * <p>Description: </p>  
 * @author Graves
 * @date 2019年2月13日   
 * @param userId
 * @param saveList  
 */ 
public void removeAllNotInclude(String userId, List<String> saveList);

public void remove(List<String> ids);


}
