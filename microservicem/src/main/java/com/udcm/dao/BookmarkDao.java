/**
 * 
 */
package com.udcm.dao;

import java.util.List;

import com.udcm.model.Bookmark;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:33:12
 */
public interface BookmarkDao {

	/**  
	 * <p>Title: getBookmarkList</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param userId
	 * @param begin
	 * @param pageSize
	 * @param sp
	 * @param contentId  
	 */ 
	List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize);
	/**  
	 * <p>Title: getCounts</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @return  
	 */ 
	Integer getCounts(String userId);

	/**  
	 * <p>Title: removeBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param pcArr  
	 */ 
	List<Bookmark> removeBookmark(String userId, Object[] pcArr);
	
	/**  
	 * cid_{cContentId}_{index}”，允许多个，兼容历史接口
	 * <p>Title: removeBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param contentId
	 * @param index  
	 */ 
	List<Bookmark> removeBookmark(String userId, String contentId, Integer index);
	
	
	/**
	 * @Title deleteFavoriteByFavoriteSize
	 * @Description 
	 * @param 
	 * @return void
	 * @throws 
	 */
	public List<Bookmark> deleteBookmarkByBookmarkSize(String userId,String size,String mediaType);

  
	List<Bookmark> getBookmarks(String userId, String stbid);

	/**  
	 * 分组查询，查询每个合集最新的一条数据
	 * <p>Title: getBookmarkListByGroup</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月15日   
	 * @param userId
	 * @param firstNum
	 * @param size
	 * @param sp
	 * @param contentId
	 * @return  
	 */ 
	List<Bookmark> getBookmarkListByGroup(String userId, Integer begin, Integer pageSize, String sp, String contentId, String date);

	/** 
	 * 删除 saveList 之外的所有记录 
	 * <p>Title: removeAllNotInclude</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年2月13日   
	 * @param userId
	 * @param saveList  
	 */ 
	void removeAllNotInclude(String userId, List<String> saveList);
	

	
	public void remove(List<String> ids);
	
	
	public List<String>getUserIds(Integer begin, Integer pageSize);
	
	public int getUserIdCount();
	
}
