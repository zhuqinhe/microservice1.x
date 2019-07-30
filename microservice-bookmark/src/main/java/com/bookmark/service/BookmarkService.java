/**
 * 
 */
package com.bookmark.service;

import java.util.List;
import java.util.Map;

import com.bookmark.model.Bookmark;

/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:35:05
 */
public interface BookmarkService {

	/**  
	 * <p>Title: add</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月12日   
	 * @param bookmark  
	 */ 
	void add(Bookmark bookmark,boolean  synchronize,String groupType) throws Exception;

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
	List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize, String sp, String contentId, String groupType,boolean synchronize)throws Exception;

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
	 * <p>Title: removebookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param pContentIds  
	 */ 
	void removeBookmark(String userId, String pContentIds,String groupType,boolean synchronize);

	/**
	 * 核对用户书签
	 * <p>Title: checkBookmark</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月13日   
	 * @param userId
	 * @param contentId
	 * @return
	 */
	List<Map<String, Object>> checkBookmark(String userId, String contentId,Integer index,String groupType,boolean synchronize);


}
