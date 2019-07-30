/**
 * 
 */
package com.udcm.dao;

import java.util.List;
import java.util.regex.Pattern;

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

import com.udcm.common.Constants;
import com.udcm.model.User;
import com.udcm.utils.StringUtils;



/**
 * @Description 
 * @author hoob
 * @date 2019年5月24日上午11:39:41
 */
@Repository("userDao")
public class UserDaoImpl implements UserDao{
	@Resource
    private MongoTemplate mongoTemplate;

	/**
	 * @Title getUserByUserId
	 * @Description 
	 * @param 
	 * @return UserDao
	 * @throws 
	 */
	@Override
	public User getUserByUserId(String userId) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(userId);
		Query query = new Query(criteria);
		return  mongoTemplate.findOne(query, User.class,Constants.USER_COL_NAME);
	}

	/**
	 * @Title updateUserInfo
	 * @Description 
	 * @param 
	 * @return UserDao
	 * @throws 
	 */
	@Override
	public User updateUserInfo(User user) {
		Criteria criteria = new Criteria();
		criteria.and("userId").is(user.getUserId());
		Query query = new Query(criteria);
		Update update=new Update();
		update.set("password",user.getPassword());
		update.set("nickName",user.getNickName());
		update.set("organization",user.getOrganization());
		update.set("lastLoginTime",user.getLastLoginTime());
		update.set("enable",user.getEnable());
		update.set("loginTimes",user.getLoginTimes());
		update.set("enableIPBinding",user.getEnableIPBinding());
		update.set("ipAddr",user.getIpAddr());
		update.set("parentUserId",user.getParentUserId());
		update.set("userlevel",user.getUserlevel());
		update.set("userType",user.getUserType());
		update.set("pwdUpdateTime",user.getPwdUpdateTime());
		update.set("createTime",user.getCreateTime());
	    return 	mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true), User.class,Constants.USER_COL_NAME);
	}

	/**
	 * @Title getUser
	 * @Description 
	 * @param 
	 * @return UserDao
	 * @throws 
	 */
	@Override
	public List<User> getUser(String userId, String name, String enable,
			String parentUserId, int begin, int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		if(StringUtils.isNotEmpty(parentUserId)){
			criteria.and("parentUserId").is(parentUserId);
		}
		if(StringUtils.isNotEmpty(enable)){
			criteria.and("enable").is(enable);
		}
		if(StringUtils.isNotEmpty(name)){
			Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
			criteria.and("name").regex(pattern);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"id")));
		query.skip(begin);
		query.limit(pageSize);
		return  mongoTemplate.find(query, User.class,Constants.USER_COL_NAME);
	}

	/**
	 * @Title counts
	 * @Description 
	 * @param 
	 * @return UserDao
	 * @throws 
	 */
	@Override
	public Integer counts(String userId, String name, String enable,
			String parentUserId) {
		        //查询条件
				Criteria criteria = new Criteria();
				if(StringUtils.isNotEmpty(userId)){
					criteria.and("userId").is(userId);
				}
				if(StringUtils.isNotEmpty(parentUserId)){
					criteria.and("parentUserId").is(parentUserId);
				}
				if(StringUtils.isNotEmpty(enable)){
					criteria.and("enable").is(enable);
				}
				if(StringUtils.isNotEmpty(name)){
					Pattern pattern=Pattern.compile("^.*"+name+".*$", Pattern.CASE_INSENSITIVE);
					criteria.and("name").regex(pattern);
				}
				Query query = new Query(criteria);
				
				return  (int) mongoTemplate.count(query, User.class,Constants.USER_COL_NAME);
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return UserDao
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		Criteria criteria = new Criteria();
		criteria.and("id").in(ids);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, User.class,Constants.USER_COL_NAME);
		
	}
}
