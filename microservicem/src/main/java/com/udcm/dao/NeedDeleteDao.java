/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.NeedToDelete;

/**
 * @Description 
 * @author hoob
 * @date 2019年6月4日上午10:04:10
 */
public interface NeedDeleteDao {
	List<NeedToDelete>getNeedToDelete(String userId,int begin,int pageSize);
	int getCount(String userId);
}
