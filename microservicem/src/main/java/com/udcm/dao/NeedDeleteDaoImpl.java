/**
 * 
 */
package com.udcm.dao;

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
import com.udcm.model.NeedToDelete;
import com.udcm.utils.StringUtils;

/**
 * @Description 
 * @author hoob
 * @date 2019年6月4日上午10:16:36
 */
@Repository("needDeleteDao")
public class NeedDeleteDaoImpl implements NeedDeleteDao{
	@Resource
	private MongoTemplate mongoTemplate;

	/**
	 * @Title getNeedToDelete
	 * @Description 
	 * @param 
	 * @return NeedDeleteDao
	 * @throws 
	 */
	@Override
	public List<NeedToDelete> getNeedToDelete(String userId,int begin, int pageSize) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.DESC,"updateTime")));
		query.skip(begin);
		query.limit(pageSize);
		return mongoTemplate.find(query,NeedToDelete.class,Constants.NEEDTODELETE_COL_NAME);
	}

	/**
	 * @Title getCount
	 * @Description 
	 * @param 
	 * @return NeedDeleteDao
	 * @throws 
	 */
	@Override
	public int getCount(String userId) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotEmpty(userId)){
			criteria.and("userId").is(userId);
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query,Constants.NEEDTODELETE_COL_NAME);
	}

}
