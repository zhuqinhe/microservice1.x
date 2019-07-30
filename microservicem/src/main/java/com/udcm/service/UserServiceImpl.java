/**
 * 
 */
package com.udcm.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.udcm.dao.UserDao;
import com.udcm.model.User;
import com.udcm.utils.UserSession;


/**
 * @Description 
 * @author hoob
 * @date 2019年5月24日上午10:56:27
 */
@Service("userService")
public class UserServiceImpl implements UserService{
	private Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);
    @Resource
    private UserDao userDao;
	
	/**
	 * @Title authorize
	 * @Description 校验用户密码，用户名是否匹配，
	 * @param 
	 * @return UserServiceImpl
	 * @throws 
	 */
	public UserSession authorize(String userId, String password) {
		if(null==userId) {
            return null;
        }
		User user  = userDao.getUserByUserId(userId);	
		if(null==user) {
            return null;
        }
		if(!password.equals(user.getPassword())){
			return null;
		}
		//构建会话
		UserSession session = new UserSession();
		Date loginTime = new Date();		
		session.setLastActiveTime(loginTime);
		session.setPassword(user.getPassword());
		session.setUserId(userId);
		return session;
	}

	/**
	 * @Title getUserByUserId
	 * @Description 通过用户ID 获取用户
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public User getUserByUserId(String userId) {
	
		return userDao.getUserByUserId(userId);	
	}

	/**
	 * @Title updateUserLoginInfo
	 * @Description 更新用户登录信息
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public boolean updateUserLoginInfo(User user) {
		//
		if(user==null){
			return false;
		}
		user.setLoginTimes(user.getLoginTimes()+1);
		user.setLastLoginTime(new Date());
		userDao.updateUserInfo(user);
		return true;
	}
	
	 public User getCurrentUser(String userToken) {
	        if (userToken != null) {

	            String[] params = userToken.split("\\:");

	            String userId = params[0]; //接口请求用户

	            User userVO = userDao.getUserByUserId(userId);

	            return userVO;

	        }
	        return null;
	    }

	/**
	 * @Title addUser
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public void addUser(User user) {
            if(user==null){
            	return ;
            }		
            user.setLastLoginTime(new Date());
            user.setPwdUpdateTime(new Date());
            user.setPassword("96e79218965eb72c92a549dd5a330112");
            user.setLoginTimes(0);
            user.setCreateTime(new Date());
            userDao.updateUserInfo(user);
	}

	/**
	 * @Title editUser
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public void editUser(User user) {
    if(user==null){
    	return ;
    }
	User t=userDao.getUserByUserId(user.getUserId());
	if(t!=null){
		t.setEnable(user.getEnable());
		t.setEnableIPBinding(user.getEnableIPBinding());
		t.setIpAddr(user.getIpAddr());
		t.setNickName(user.getNickName());
		t.setOrganization(user.getOrganization());
		userDao.updateUserInfo(t);
	  }	
	}

	/**
	 * @Title getUser
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public List<User> getUser(String userId, String name, String enable,
			String parentUserId, int begin, int pageSize) {
		if("sysadmin".equals(parentUserId)){
			parentUserId=null;
		}
		return userDao.getUser(userId, name, enable, parentUserId, begin, pageSize);
	}

	/**
	 * @Title counts
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public Integer counts(String userId, String name, String enable,
			String parentUserId) {
		if("sysadmin".equals(parentUserId)){
			parentUserId=null;
		}
		return userDao.counts(userId, name, enable, parentUserId);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		userDao.remove(ids);
	}

	/**
	 * @Title enableUser
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public boolean enableUser(User user) {
		if(user==null){
			return false;
		}
		User t=userDao.getUserByUserId(user.getUserId());
		t.setEnable(!user.getEnable());
		userDao.updateUserInfo(t);
		return true;
	}

	/**
	 * @Title updatePassword
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public void updatePassword(User user) {
		if(user==null){
			return ;
		}
		User t=userDao.getUserByUserId(user.getUserId());
		t.setPassword(user.getPassword());
		t.setPwdUpdateTime(new Date());
		userDao.updateUserInfo(t);
	}

	/**
	 * @Title resetPassword
	 * @Description 
	 * @param 
	 * @return UserService
	 * @throws 
	 */
	@Override
	public void resetPassword(User user) {
		if(user==null){
			return ;
		}
		User t=userDao.getUserByUserId(user.getUserId());
		t.setPassword("96e79218965eb72c92a549dd5a330112");
		t.setPwdUpdateTime(new Date());
		userDao.updateUserInfo(t);
		
	}
}
