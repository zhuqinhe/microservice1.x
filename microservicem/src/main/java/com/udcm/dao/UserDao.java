/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.User;

/**
 * @Description 
 * @author hoob
 * @date 2019年5月24日上午11:39:27
 */
public interface UserDao {	
User getUserByUserId(String userId);
User updateUserInfo(User user);
public List<User> getUser(String userId, String name, String enable,String parentUserId, int begin, int pageSize);
public Integer counts(String userId, String name, String enable,String parentUserId);
public void remove(List<String> ids);
}
