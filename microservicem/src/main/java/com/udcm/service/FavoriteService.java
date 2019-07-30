/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.Favorite;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月11日下午08:19:55
 */
public interface FavoriteService {
public List<Favorite> getFavoriteList(String userId, Integer begin, Integer pageSize) throws  Exception;
public  long getFavoriteCount(String userId,String mediaType);
public void remove(String userId,String contentIds,String mediaType,String groupType,boolean synchronize);
public void remove(List<String>ids);
}
