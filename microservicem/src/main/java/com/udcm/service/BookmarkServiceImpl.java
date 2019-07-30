/**
 * 
 */
package com.udcm.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.udcm.common.ConfigKey;
import com.udcm.common.Constants;
import com.udcm.dao.BookmarkDao;
import com.udcm.dao.NeedDeleteDao;
import com.udcm.model.Bookmark;
import com.udcm.model.NeedToDelete;
import com.udcm.utils.ConfigUtil;
import com.udcm.utils.StringUtils;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:35:20
 */
@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService{
	private Logger logger = LoggerFactory.getLogger(FavoriteServiceImpl.class);
	@Resource
	private BookmarkDao bookmarkDao;
	//@Resource
	//private MqService mqService;
	//@Resource 
	//private KafkaService kafkaService;
	
	@Resource
	private NeedDeleteDao needDeleteDao;
	
	
	

	@Override
	public List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize) throws Exception{

		//如果begin和pageSize为空，设置默认值
		Integer firstNum = StringUtils.obj2Int(begin, Constants.BEGIN);
		Integer size = StringUtils.obj2Int(pageSize,Constants.PAGESIZE);
		return bookmarkDao.getBookmarkList(userId, firstNum, size);
		
		

	}
	
	@Override
	public Integer getCounts(String userId) {
		
		return bookmarkDao.getCounts(userId);
	}

	@Override
	public void removeBookmark(String userId, String pContentIds,String groupType,boolean synchronize) {
		
		String[] pcArr = pContentIds.trim().split(",");
		List<Bookmark>deleteList=new ArrayList<Bookmark>();
		
		deleteList=bookmarkDao.removeBookmark(userId, pcArr);
		
	    if(synchronize&&"mango".equals(ConfigUtil.getProperties(ConfigKey.SCENE.name()))&&deleteList!=null&&!deleteList.isEmpty()){
		    	for(Bookmark bookmark:deleteList){
		    	//	mqService.sendBookmarkMessage(bookmark,Constants.ACTION_DELETE);
		    	//	kafkaService.sendBookmarkMessage(bookmark, Constants.ACTION_DELETE);
		    	}
		    }
	}

	/**
	 * @Title remove
	 * @Description 
	 * @param 
	 * @return BookmarkService
	 * @throws 
	 */
	@Override
	public void remove(List<String> ids) {
		bookmarkDao.remove(ids);
	}

	/**
	 * @Title getUserIds
	 * @Description 
	 * @param 
	 * @return BookmarkService
	 * @throws 
	 */
	@Override
	public List<NeedToDelete> getUserIds(String userId,Integer begin, Integer pageSize) {
		// TODO Auto-generated method stub
		//return bookmarkDao.getUserIds(begin, pageSize);
		return needDeleteDao.getNeedToDelete(userId,begin, pageSize);
	}

	/**
	 * @Title getUserIdCount
	 * @Description 
	 * @param 
	 * @return BookmarkService
	 * @throws 
	 */
	@Override
	public int getUserIdCount(String userId) {
		//return bookmarkDao.getUserIdCount();
		return needDeleteDao.getCount(userId);
	}
}
