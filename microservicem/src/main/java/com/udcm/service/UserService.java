/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.User;
import com.udcm.utils.UserSession;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月24日上午10:56:03
 */
public interface UserService {
	UserSession authorize(String userId, String password);
	User getUserByUserId(String userId);
	boolean updateUserLoginInfo(User user);
	public User getCurrentUser(String userToken);
	void addUser(User user);
	void editUser(User user);
	List<User>getUser(String userId,String name,String enable,String parentUserId,int begin,int pageSize);
	public Integer counts(String userId, String name, String enable,String parentUserId);
	public void remove(List<String> ids);
	public boolean enableUser(User user);
	public void updatePassword(User user);
	public void resetPassword(User user);
}
