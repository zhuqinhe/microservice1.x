/**
 * 
 */
package com.bookmark.dao;

import java.util.List;

import com.bookmark.model.Bookmark;



/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:33:12
 */
public interface BookmarkDao {

	/**  
	 * <p>Title: add</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param bookmark  
	 */ 
	Bookmark add(Bookmark bookmark);

	/**  
	 * <p>Title: getBookmarkList</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param userId
	 * @param begin
	 * @param pageSize
	 * @param sp  
	 */ 
	List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize, String sp, String contentId, String date);
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
	List<Bookmark> removeBookmark(String userId, String[] pcArr);
	
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
	 * <p>Title: checkBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param contentId
	 * @return  
	 */ 
	List<Bookmark> checkBookmark(String userId, String contentId,Integer index);
	
	/**
	 * @Title deleteFavoriteByFavoriteSize
	 * @Description 
	 * @param 
	 * @return void
	 * @throws 
	 */
	public List<Bookmark> deleteBookmarkByBookmarkSize(String userId,String size,String mediaType);

    public Bookmark addOrUpdateSingleBookmark(Bookmark bookmark);

	/**  
	 * <p>Title: getBookmarks</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月14日   
	 * @param userId
	 * @param stbid
	 * @return  
	 */ 
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
	 * @return  
	 */ 
	List<Bookmark> getBookmarkListByGroup(String userId, Integer begin, Integer pageSize, String sp, String contentId, String date);

}
