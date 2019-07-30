/**
 * 
 */
package com.favorite.service;

import java.util.List;

import com.favorite.model.Favorite;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午08:19:55
 */
public interface FavoriteService {
/**
 * @Title add
 * @Description 
 * @param 
 * @return void
 * @throws 
 */
public void add(Favorite favorite,boolean  synchronize,String groupType);

public List<Favorite> getFavoriteList(String userId, Integer begin, Integer pageSize, String sp, String date,String mediatype,String groupType,boolean synchronize) throws  Exception;
public  long getFavoriteCount(String userId,String mediaType);
public void remove(String userId,String contentIds,String mediaType,String groupType,boolean synchronize);
public boolean checkFavorite(String userId, String contentId,String mediatype,String groupType,boolean synchronize);
}
