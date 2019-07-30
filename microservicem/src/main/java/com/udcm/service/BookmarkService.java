/**
 * 
 */
package com.udcm.service;

import java.util.List;

import com.udcm.model.Bookmark;
import com.udcm.model.NeedToDelete;
/**
 * @Description 
 * @author hoob
 * @date 2018年11月12日下午1:35:05
 */
public interface BookmarkService {
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
	List<Bookmark> getBookmarkList(String userId, Integer begin, Integer pageSize)throws Exception;

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
	 * @Title remove
	 * @Description 根据Id 批量删除记录
	 * @param 
	 * @return void
	 * @throws 
	 */
	public void remove(List<String> ids);
	
    /**
     * @Title getUserIds
     * @Description 获取用户
     * @param 
     * @return List<String>
     * @throws 
     */
    public List<NeedToDelete>getUserIds(String userId,Integer begin, Integer pageSize);
	
	/**
	 * @Title getUserIdCount
	 * @Description 获取用户ID总数
	 * @param 
	 * @return int
	 * @throws 
	 */
	public int getUserIdCount(String userId);
	
	
}
