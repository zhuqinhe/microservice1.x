/**
 * 
 */
package com.fonsview.metadata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fonsview.metadata.config.Config;
import com.fonsview.metadata.utils.JsonUtils;
import com.fonsview.metadata.utils.StringUtils;




/**
 * @Description 
 * @author hoob
 * @date 2018年8月3日上午11:47:53
 */
@Repository("metadataDao")
public class MetadataDaoImpl implements MetadataDao{
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private Config config;

	/**
	 * @Title getChannelList
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getChannelList(String cpId, String type,String filter,String language) {
		List<Object>param=new ArrayList<Object>();
		StringBuilder getChannelList_sql = new StringBuilder(
				"SELECT ch.mediaType as mediaType,ch.contentId,ch.cpCode,ch.name,ch.subName,"
						+ "'' score,ch.logoUrl as thumbnailURL,ch.logoUrl as posterURL,ch.tags,'' as cornerMark,"
						+ "ch.liveType as programType,'' as kinds, ch.rating as rating,0 'release',"
						+ "ch.description,ch.cpId  as sp,ch.areaCode as area,ch.channelNumber as channelNum,"
						+ "ch.channelType,ch.supportTimeshift,ch.timeshiftDuration,ch.playbackDuration,"
						+ "ch.timeshiftLeftDeviation,ch.timeshiftMinIncrement,ch.timeshiftMaxIncrement,"
						+ "ch.supportPlayback "
						+ "FROM channel ch "
						+ " WHERE ch.status=1 ");
		if(StringUtils.isNotEmpty(cpId)){
			getChannelList_sql.append(" and ch.cpId=?");
			param.add(cpId);
		}
		if(StringUtils.isNotEmpty(type)){
			if("TV".equalsIgnoreCase(type)){
				getChannelList_sql.append(" and ch.channelType=0");
			}else if("VCC".equalsIgnoreCase(type)){
				getChannelList_sql.append(" and ch.channelType=1");
			}else if("Radio".equalsIgnoreCase(type)){
				getChannelList_sql.append(" and ch.channelType=2");
			}else if("ALL".equalsIgnoreCase(type)){
				getChannelList_sql.append(" and (ch.channelType=0 or ch.channelType=1) ");
			}
		}
		if(StringUtils.isNotEmpty(filter)){
			//获取所有的筛选条件
			List<Map<String,Object>>filters=this.getFilterList(filter,language);
			if(filters!=null&&!filters.isEmpty()){
				for(Map<String,Object> tmp:filters){
					if(tmp.get("filterDetail")!=null){
						getChannelList_sql.append(" " +tmp.get("filterDetail")+" ");
					}
				}
			}
		}
		getChannelList_sql.append( "  ORDER BY ch.channelNumber asc ");
		return this.jdbcTemplate.queryForList(getChannelList_sql.toString(),param.toArray());
	}
	/**
	 * @Title getFilterList
	 * @Description  获取filter排序
	 * @param codes 逗号间隔
	 * @return List<Filter>
	 * @throws 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>>getFilterList(String codes,String language){
		if(StringUtils.isEmpty(codes))return null;
		String sql="select fm.id,fm.sequence,f.listOrder,f.code,f.categories,f.labelType,fm.filterDetail,i18n.name as listName,i18n.language as defaultLanguage,fm.name as name from filter f left join i18n_filter i18n on f.code=i18n.code left join filterparam fm on fm.filterCode=f.code where fm.code in(:codes) and i18n.language=:language ";
		HashMap<String,Object>map=new HashMap<String,Object>();
		map.put("codes",Arrays.asList(codes.split(",")));
		map.put("language",language);
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
		return jdbc.queryForList(sql,map);

	}
	/*private RowMapper getFilterList = new RowMapper<Filter>(){

		@Override
		public Filter mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Filter s=new Filter();
			s.setId(rs.getLong("id"));
			s.setListOrder(rs.getInt("listOrder"));
			s.setCode(rs.getString("code"));
			s.setListName(rs.getString("listName"));
			s.setDefaultLanguage(rs.getString("language"));
			HashMap<String,String>map=JsonUtils.json2Obj(rs.getString("name"), HashMap.class);
			s.setName(map==null?"":map.get(s.getDefaultLanguage()));
			s.setCategories(rs.getString("categories"));
			s.setLabelType(rs.getInt("labelType"));
			s.setSequence(rs.getInt("sequence"));
			s.setFilterDetail(rs.getString("filterDetail"));
			return s;
		}
	};*/

	/**
	 * @Title getSchedulList
	 * @Description 获取节目单列表
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getSchedulList(String channelContentId,
			String starTime, String endTime) {
		Map<String,Object>param=new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder(
				"SELECT s.contentId scheduleId,s.name scheduleName,s.date,s.startTime,"
						+ "s.endTime,s.description,s.channelContentId channelId,s.picUrl posterUrl "
						+ "FROM schedulerecord s "
						+ " WHERE 1=1  ");
		if(StringUtils.isNotEmpty(channelContentId)&&!"0".equals(channelContentId)){
			sql.append(" and  s.channelContentId in (:contentId)");
			param.put("contentId", Arrays.asList(channelContentId.split(",")));

		}
		if(StringUtils.isNotEmpty(starTime)){
			sql.append(" and s.date>=:starTime ");
			param.put("starTime",starTime.replace("-",""));

		}
		if(StringUtils.isNotEmpty(endTime)){
			sql.append(" and  s.date<=:endTime ");
			param.put("endTime",endTime.replace("-",""));
		}
		sql.append(" ORDER BY s.date, s.startTime ");
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);

		return jdbc.queryForList(sql.toString(),param);
	}
	/**
	 * @Title getCategorysByContentId
	 * @Description 获取栏目列表
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getCategorysByContentId(String contentId,String cpId) {
		StringBuilder sql=new StringBuilder();
		ArrayList<Object> params=new ArrayList<Object>();
		sql.append("SELECT c.mediaType,c.contentId as  categoryId,c.contentId,c.model,c.description,c.name as categoryName,c.picture as thumbnailURL,c.pictureinfo,c.backgroundImage as bgImageURL,"
				+ " c.parentContentId as parentId,c.styleCode as style,c.identifier,c.subName,c.identityno,(select count(id) from category cs where cs.parentContentId =c.contentId and cs.`enable`=1 ) as subCategoryNumber "
				+ " ,c.supportFilter,c.supportSearch,c.styleCode as style,c.epsdDsply as episodeDisplay,c.epsdOdr as episodeSort FROM category c "
				+ "  where enable=1  and c.model not in(1,7) ");
		if(StringUtils.isNotEmpty(contentId)){
			params.add(contentId);
			sql.append(" and c.parentContentId=?");
		}
		if(StringUtils.isNotEmpty(cpId)){
			params.add(cpId);
			sql.append(" and c.cpId=?");
		}
		sql.append(" ORDER BY c.sortNum asc ");
		return this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
	}

	/**
	 * @Title getSortByCategory
	 * @Description 用于判断父栏目下的子栏目数量
	 * @param categoryContentId
	 * @return int
	 * @throws 
	 */
	public List<Map<String,Object>> getSortByCategory(String categoryContentId){
		if(StringUtils.isEmpty(categoryContentId))return null;
		String sql="select s.code,s.name from sort s  where FIND_IN_SET(?,categories) ";
		ArrayList<Object>params=new ArrayList<Object>();
		params.add(categoryContentId);
		return jdbcTemplate.queryForList(sql.toString(),params.toArray());

	}


	/**
	 * @Title getFilterByCategory
	 * @Description 用于判断父栏目下的子栏目数量
	 * @param categoryContentId
	 * @return int
	 * @throws 
	 */

	public List<Map<String,Object>> getFilterByCategory(String categoryContentId){
		if(StringUtils.isEmpty(categoryContentId)){return null;}
		String sql="select f.code,f.name,f.defaultLanguage from filter f  where FIND_IN_SET(?,categories)  order by f.listOrder desc ";
		ArrayList<Object>params=new ArrayList<Object>();
		params.add(categoryContentId);
		List<Map<String,Object>>maps=this.jdbcTemplate.queryForList(sql,params.toArray());
		if(maps!=null&&!maps.isEmpty()){
			//组装数据
			for(Map<String,Object>m:maps){
				//只要code 和   名称
				if(m.get("code")!=null&&StringUtils.isNotEmpty(m.get("code").toString())){
					sql="select DISTINCT name,code from filterparam where filterCode=? order by  sequence desc";
					params.clear();
					params.add(m.get("code").toString());
					List<Map<String,Object>>map=this.jdbcTemplate.queryForList(sql, params.toArray());
					//处理map的name值
					if(map!=null&&!map.isEmpty()){
						for(Map<String,Object>m2:map ){
							if(m2.get("name")!=null&&StringUtils.isNotEmpty(m2.get("name").toString())){
								Map<String,String> names=JsonUtils.json2Obj(m2.get("name").toString(), Map.class);
								m2.put("name", names.get(m.get("defaultLanguage")));//获取对应语种的名称(这里是默认语言的)
							}
						}
					}
					m.remove("code");//只保留name属性
					m.put("filterList", map);
				}
			}
		}
		return maps;
	}
	/**
	 * @Title getCategorydetail
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public Map<String, Object> getCategorydetail(String categoryContentId) {
		if(StringUtils.isEmpty(categoryContentId)){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		List<Object>params=new ArrayList<Object>();
		sql.append("SELECT c.mediaType,c.contentId as  categoryId,c.contentId,c.model,c.description,c.name as categoryName,c.picture as thumbnailURL,c.pictureinfo,c.backgroundImage as bgImageURL,"
				+ " c.parentContentId as parentId,c.styleCode as style,c.identifier,c.subName,c.identityno,(select count(id) from category cs where cs.parentContentId =c.contentId and cs.`enable`=1 ) as subCategoryNumber "
				+ " ,c.supportFilter,c.supportSearch,c.styleCode as style,c.epsdDsply as episodeDisplay,c.epsdOdr as episodeSort FROM category c "
				+ "  where c.enable=1 and c.model not in(1,7) ");
		if(StringUtils.isNotEmpty(categoryContentId)){
			params.add(categoryContentId);
			sql.append(" and c.contentId=?");
		}
		List<Map<String,Object>>result=this.jdbcTemplate.queryForList(sql.toString(),params.toArray());
		if(result!=null&&result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	/**
	 * @Title getMediaByCategory
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediaByCategorySeries(String sortsql,String filtersql, String identityno,String language, int pageSize, int begin) {
		if(StringUtils.isEmpty(identityno)){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		List<Object>params=new ArrayList<Object>();
		//合集侧
		sql.append("SELECT  s.mediaType,s.contentId,s.cpCode,s.name,s.subName,s.actors as thirdName,"
				+ "s.directors as fourthName,'0' as fifthName,s.totalNumber as sixthName,s.language,"
				+ "s.updatedNumber as seventhName,'0' as eighthName,s.score,s.detailPicUrl as thumbnailUrl,"
				+ "s.posterPicUrl as posterUrl,s.tags,s.corner as cornerMark,s.vodType as programType,"
				+ "s.genre as kinds,s.rating as rating,s.releaseYear 'release',"
				+ "s.intro as description,s.cpId as sp,s.areaCode as area "
				+ " from series s "
				+ " left join category_association ca on s.contentId=ca.objectContentId and s.mediaType=ca.objectMediaType "
				+ "  where ca.categoryIdentityno like ? and s.status=1 ");
		params.add(StringUtils.likeSearchStr(identityno));
		if(StringUtils.isNotEmpty(filtersql)){
			//获取所有的筛选条件
			List<Map<String,Object>>filters=this.getFilterList(filtersql,language);
			if(filters!=null&&!filters.isEmpty()){
				for(Map<String,Object> tmp:filters){
					if(tmp.get("filterDetail")!=null){
						sql.append(" " +tmp.get("filterDetail")+" ");
					}
				}
			}
		}
		List<Map<String,Object>>sorts=this.getSortList(sortsql,language);
		if(sorts!=null&&!sorts.isEmpty()){
			sql.append(sorts.get(0).get("sortDetail"));
		}else{
			sql.append(" order by ca.top desc,ca.sortNum desc,s.id desc");
		}
		sql.append( "   limit ?,? " );
		params.add(begin);
		params.add(pageSize);
		return this.jdbcTemplate.queryForList(sql.toString(),params.toArray());

	}

	public int getMediaByCategorySeriesCount(String sortsql,String filtersql, String identityno,String language) {
		if(StringUtils.isEmpty(identityno)){
			return 0;
		}
		StringBuilder sql=new StringBuilder();
		List<Object>params=new ArrayList<Object>();
		//合集侧
		sql.append("SELECT  count(DISTINCT s.id) from series s "
				+ " left join category_association ca on s.contentId=ca.objectContentId and s.mediaType=ca.objectMediaType "
				+ " where ca.categoryIdentityno like ? and s.status=1  ");
		params.add(StringUtils.likeSearchStr(identityno));
		if(StringUtils.isNotEmpty(filtersql)){
			//获取所有的筛选条件
			List<Map<String,Object>>filters=this.getFilterList(filtersql,language);
			if(filters!=null&&!filters.isEmpty()){
				for(Map<String,Object> tmp:filters){
					if(tmp.get("filterDetail")!=null){
						sql.append(" " +tmp.get("filterDetail")+" ");
					}
				}
			}
		}
		return this.jdbcTemplate.queryForObject(sql.toString(),params.toArray(),Integer.class);
	}
	/**
	 * @Title getMediaByCategoryChannel
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediaByCategoryChannel(String sortsql, String filtersql,String identityno,String language, int pageSize, int begin) {
		if(StringUtils.isEmpty(identityno)){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		List<Object>params=new ArrayList<Object>();
		//合集侧
		sql.append(	" SELECT  ch.mediaType,ch.contentId,ch.name,ch.subName,ch.channelNumber as thirdName,ch.supportTimeshift as  fourthName,ch.cpCode,"
				+ "ch.timeshiftLeftDeviation as fifthName,ch.timeshiftDuration as sixthName,ch.supportPlayback as  seventhName,ch.playbackDuration as eighthName,0 as  score,ch.logoUrl as thumbnailURL,ch.logoUrl as posterURL,ch.tags,"
				+ "ch.corner as cornerMark,ch.liveType as programType,'' kinds,ch.rating as rating,"
				+ "'0' as 'release',ch.description,ch.cpId sp,ch.areaCode as area "
				+ " from channel ch "
				+ " left join category_association ca on ca.objectContentId=ch.contentId and ch.mediaType=ca.objectMediaType "
				+ " where  ca.categoryIdentityno like ?  and ch.status=1  ");
		params.add(StringUtils.likeSearchStr(identityno));
		if(StringUtils.isNotEmpty(filtersql)){
			//获取所有的筛选条件
			List<Map<String,Object>>filters=this.getFilterList(filtersql,language);
			if(filters!=null&&!filters.isEmpty()){
				for(Map<String,Object> tmp:filters){
					if(tmp.get("filterDetail")!=null){
						sql.append(" " +tmp.get("filterDetail")+" ");
					}
				}
			}
		}

		List<Map<String,Object>>sorts=this.getSortList(sortsql,language);
		if(sorts!=null&&!sorts.isEmpty()){
			sql.append(sorts.get(0).get("sortDetail"));
		}else{
			sql.append(" order ca.top desc,ca.sortNum desc,ch.id desc");
		}
		sql.append( "   limit ?,? " );
		params.add(begin);
		params.add(pageSize);
		return this.jdbcTemplate.queryForList(sql.toString(),params.toArray());

	}

	public int getMediaByCategoryChannelCount(String sortsql, String filtersql,String identityno,String language) {
		if(StringUtils.isEmpty(identityno)){
			return 0;
		}
		StringBuilder sql=new StringBuilder();
		List<Object>params=new ArrayList<Object>();
		//合集侧
		sql.append(	" SELECT count(DISTINCT ch.id)  "
				+ " from channel ch "
				+ " left join category_association ca on ca.objectContentId=ch.contentId and ch.mediaType=ca.objectMediaType "
				+ " where  ca.categoryIdentityno like ? and ch.status=0 ");
		params.add(StringUtils.likeSearchStr(identityno));
		if(StringUtils.isNotEmpty(filtersql)){
			//获取所有的筛选条件
			List<Map<String,Object>>filters=this.getFilterList(filtersql,language);
			if(filters!=null&&!filters.isEmpty()){
				for(Map<String,Object> tmp:filters){
					if(tmp.get("filterDetail")!=null){
						sql.append(" " +tmp.get("filterDetail")+" ");
					}
				}
			}
		}
		return this.jdbcTemplate.queryForObject(sql.toString(),params.toArray(),Integer.class);

	}
	/**
	 * @Title getMVodPlayURL
	 * @Description 获取movie媒体内容的播放地址
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMVodPlayURL(String mcontentIs) {
		if(StringUtils.isEmpty(mcontentIs)){
			return null;
		}
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("contentIds",Arrays.asList(mcontentIs.split(",")));
		String getMediacontentPlayInfo_sql = " "
				+ "SELECT m.contentId mContentID,m.playUrl playURL,pm.number 'index',m.cpId "
				+ "FROM movie m  "
				+ "LEFT JOIN program_movie pm ON pm.movieContentId=m.contentId "
				+ "LEFT JOIN dist_status_movie dm ON dm.contentId=m.contentId "
				+ "WHERE m.contentId in (:contentIds) AND dm.distType=0 AND (dm.opStatus=2 "
				+ "OR dm.opStatus=6) AND m.recycle=0 ";
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);

		return  jdbc.queryForList(getMediacontentPlayInfo_sql,params);
	}
	/**
	 * @Title getSVodPlayURL
	 * @Description 通过合集的contentId获取播放地址
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getSVodPlayURL(String scontentId) {
		if(StringUtils.isEmpty(scontentId)){
			return null;
		}
		List<Object>params=new ArrayList<Object>();
		params.add(scontentId);
		String getPlayInfoBySeriesID_sql = " "
				+ "SELECT m.contentId mContentID,m.playUrl playURL,pm.number 'index',m.cpId "
				+ "FROM movie m "
				+ "LEFT JOIN program_movie pm ON pm.movieContentId=m.contentId "
				+ "LEFT JOIN series_program sp ON sp.programContentId=pm.programContentId "
				+ "WHERE sp.seriesContentId=?   ORDER BY pm.number ";
		return  this.jdbcTemplate.queryForList(getPlayInfoBySeriesID_sql,params.toArray());
	}

	/**
	 * @Title getChannlePlayURL
	 * @Description 获取直播的播放地址
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>getChannlePlayURL(String scontentIds){
		Map<String,Object>map=new HashMap<String,Object>();
		StringBuilder sql =new StringBuilder();
		sql.append( "SELECT c.contentId,c.playUrl playURL,c.timeshiftUrl timeshiftURL,c.cpId "
				+ "FROM channel c "
				+ "WHERE    c.status=1  ");
		if(StringUtils.isNotEmpty(scontentIds)){
			sql.append(" and  c.contentId in(:contentId)");
			map.put("contentId",Arrays.asList(scontentIds.split(",")));
		}
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
		return jdbc.queryForList(sql.toString(),map);
	}
	/**
	 * @Title getLabels
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getLabels(String type) {
		if(StringUtils.isEmpty(type)){
			return null;
		}
		String getLabel_sql = "SELECT l.name,l.code, i18n.picUrl imageURL FROM label l left join i18n_label i18n on i18n.code=l.code  where "
				+ " i18n.language=l.defaultLanguage and  l.type=?";
		List<Object>params=new ArrayList<Object>();
		params.add(type);
		return this.jdbcTemplate.queryForList(getLabel_sql,params.toArray());
	}
	/**
	 * @Title getFilters
	 * @Description 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getFilters(String type) {
		if(StringUtils.isEmpty(type)){
			return null;
		}
		String sql="select f.code,f.name,f.defaultLanguage from filter f   order by f.listOrder desc ";
		ArrayList<Object>params=new ArrayList<Object>();
		params.add(type);
		List<Map<String,Object>>maps=this.jdbcTemplate.queryForList(sql);
		if(maps!=null&&!maps.isEmpty()){
			//组装数据
			for(Map<String,Object>m:maps){
				//只要code 和   名称
				if(m.get("code")!=null&&StringUtils.isNotEmpty(m.get("code").toString())){
					sql="select DISTINCT name,code from filterparam where filterCode=? order by  sequence desc";
					params.clear();
					params.add(m.get("code").toString());
					List<Map<String,Object>>map=this.jdbcTemplate.queryForList(sql, params.toArray());
					//处理map的name值
					if(map!=null&&!map.isEmpty()){
						for(Map<String,Object>m2:map ){
							if(m2.get("name")!=null&&StringUtils.isNotEmpty(m2.get("name").toString())){
								Map<String,String> names=JsonUtils.json2Obj(m2.get("name").toString(), Map.class);
								m2.put("name", names.get(m.get("defaultLanguage")));//获取对应语种的名称(这里是默认语言的)
							}
						}
					}
					m.remove("code");//只保留name属性
					m.put("filterList", map);
				}
			}
		}
		return maps;
	}

	/**
	 * @Title getSortList
	 * @Description  获取Sort排序
	 * @param codes 逗号间隔
	 * @return List<Sort>
	 * @throws 
	 */
	public List<Map<String,Object>> getSortList(String codes,String language){
		if(StringUtils.isEmpty(codes))return null;
		String sql="select i18n.name,s.code,s.sortDetail,i18n.language,s.categories  from sort s left join  i18n_sort i18n  on i18n.code=s.code where s.code in(:codes) and i18n.language=:language ";
		HashMap<String,Object>map=new HashMap<String,Object>();
		map.put("codes",Arrays.asList(codes.split(",")));
		map.put("language",language);
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
		return jdbc.queryForList(sql,map);
	}
	/*private RowMapper getSortList = new RowMapper<Sort>(){
		@Override
		public Sort mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Sort m = new Sort();
			m.setCategories(rs.getString("categories"));
			m.setCode(rs.getString("code"));
			m.setLanguage(rs.getString("language"));
			m.setName(rs.getString("name"));
			m.setSortDetail(rs.getString("sortDetail"));
			return m;
		}
	};
	 */
	/**
	 * @Title getSpecialByCategory
	 * @Description 获取指定栏目下的专题
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getSpecialByCategory(String categoryContentId) {
		if(StringUtils.isEmpty(categoryContentId)){
			return null;
		}
		List<Object>params=new ArrayList<Object>();
		params.add(categoryContentId);
		String sql = " "
				+ "SELECT c.contentId,c.name,c.subName,c.picture thumbnailURL,"
				+ "c.backgroundImage bgImageURL,c.styleCode style "
				+ "FROM category c "
				+ "LEFT JOIN category_association ca ON ca.objectContentId = c.contentId "
				+ "WHERE ca.categoryContentId=? AND c.enable=1  "
				+ "ORDER BY ca.sortNum desc";
		return this.jdbcTemplate.queryForList(sql,params.toArray());
	}
	/**
	 * @Title getMediasBycontentId
	 * @Description 根据contentId获取媒体内容详情
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getMediasBycontentId(String contentIds) {
		if(StringUtils.isEmpty(contentIds)){
			return null;
		}
		HashMap<String,Object>param=new HashMap<String,Object>();
		param.put("contentIds",Arrays.asList(contentIds.split(",")));
		String mediaData = ""
				+ "SELECT '2' mediaType,s.contentId,s.cpCode,s.name,s.subName,s.actors thirdName,"
				+ "s.directors fourthName,'0' fifthName,concat(s.totalNumber,'') sixthName,s.language,"
				+ "concat(s.updatedNumber,'') seventhName,'0' eighthName,s.score,s.detailPicUrl "
				+ "thumbnailUrl,s.posterPicUrl posterUrl,s.tags,s.corner cornerMark,s.vodType "
				+ "programType,s.genre kinds,concat(s.rating,'') rating,s.releaseYear 'release',"
				+ "s.intro description,s.cpId sp,s.areaCode area from series s "
				+ " where s.contentId in (:contentIds) and  s.status=1";
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
		return jdbc.queryForList(mediaData,param);
	}
	/**
	 * @Title getMediaDetailBycontentId
	 * @Description //获取内容详情 
	 * @param 
	 * @return MetadataV1Dao
	 * @throws 
	 */
	@Override
	public Map<String, Object> getMediaDetailBycontentId(String contentId,String code) {
		if(StringUtils.isEmpty(contentId)&&StringUtils.isEmpty(code)){
			return null;
		}
		//先查询直播
		List<Object>param=new ArrayList<Object>();
		StringBuilder channel=new StringBuilder( "SELECT '3' mediaType,ch.contentId,ch.name,ch.subName,concat(ch.channelNumber,'') "
				+ "thirdName,concat(ch.supportTimeshift,'') fourthName,ch.cpCode,"
				+ "concat(ch.timeshiftLeftDeviation,'') fifthName,concat(ch.timeshiftDuration,'') "
				+ "sixthName,concat(ch.supportPlayback,'') seventhName,concat(ch.playbackDuration,'') "
				+ "eighthName,0 score,ch.logoUrl thumbnailUrl,ch.logoUrl posterUrl,ch.tags,ch.corner "
				+ "cornerMark,ch.liveType programType,'' kinds,concat(ch.rating,'') rating,"
				+ "'0' as 'release',ch.description,ch.cpId sp,ch.areaCode area ,channelNumber channelNum,channelType,supportTimeshift,"
				+ "timeshiftDuration,timeshiftLeftDeviation,timeshiftMinIncrement,"
				+ "timeshiftMaxIncrement,supportPlayback,playbackDuration "
				+ "FROM channel ch ");
		if(StringUtils.isNotEmpty(code)){
			channel.append( "  where ch.cpCode =? ");
			param.add(code);
		}else{
			channel.append( "  where ch.contentId =? ");
			param.add(contentId);
		}
		channel.append(" and ch.status=1");
		List<Map<String,Object>>result=this.jdbcTemplate.queryForList(channel.toString(),param.toArray());
		if(result!=null&&result.size()>0){
			//有直播,处理播放地址  ，图片
			Map<String,Object>channelmap=result.get(0);//默认第一个

			if(channelmap.get("thumbnailUrl")!=null&&StringUtils.isNotEmpty(channelmap.get("thumbnailUrl").toString())){
				channelmap.put("thumbnailUrl", config.getPicture_url_prefix()+"/"+channelmap.get("thumbnailUrl"));
			}
			if(channelmap.get("posterUrl")!=null&&StringUtils.isNotEmpty(channelmap.get("posterUrl").toString())){
				channelmap.put("posterUrl", config.getPicture_url_prefix()+"/"+channelmap.get("posterUrl"));
			}
			//处理播放地址,时移地址(不处理播放地址问题，有独立接口获取对应的播放地址)
			//channelmap.put("playUrl",Utils.processJsonUrl(request, channelmap.get("playUrl")));
			//channelmap.put("timeshiftURL", Utils.processJsonUrl(request,channelmap.get("timeshiftURL").toString()));
			//处理内容上的栏目
			String getCategoryCIDByCID_sql = "SELECT ca.categoryContentId FROM category_association ca  WHERE ca.objectContentId=?  limit 1";
			param.clear();
			param.add(contentId);
			String categorys=this.jdbcTemplate.queryForObject(getCategoryCIDByCID_sql,param.toArray() ,String.class);
			channelmap.put("categories", categorys);
			return channelmap;
		}

		//如果直播没有找到合集
		param.clear();
		channel=new StringBuilder("SELECT '2' mediaType,s.contentId,s.cpCode,s.name,s.subName,s.actors thirdName,"
				+ "s.directors fourthName,'0' fifthName,concat(s.totalNumber,'') sixthName,s.language,"
				+ "concat(s.updatedNumber,'') seventhName,'0' eighthName,s.score,s.detailPicUrl "
				+ "thumbnailUrl,s.posterPicUrl posterUrl,s.tags,s.corner cornerMark,s.vodType "
				+ "programType,s.genre kinds,concat(s.rating,'') rating,s.releaseYear 'release',"
				+ "s.intro description,s.cpId sp,s.areaCode area ,totalNumber totalEpisodes "
				+ "FROM series s ");
		if(StringUtils.isNotEmpty(code)){
			channel.append( "  where s.cpCode =? ");
			param.add(code);
		}else{
			channel.append( "  where s.contentId =? ");
			param.add(contentId);
		}
		channel.append(" and s.status=1 ");
		result=this.jdbcTemplate.queryForList(channel.toString(),param.toArray());
		if(result!=null&&result.size()>0){
			Map<String,Object>seriesmap=result.get(0);
			if(seriesmap.get("thumbnailUrl")!=null&&StringUtils.isNotEmpty(seriesmap.get("thumbnailUrl").toString())){
				seriesmap.put("thumbnailUrl", config.getPicture_url_prefix()+"/"+seriesmap.get("thumbnailUrl"));
			}
			if(seriesmap.get("posterUrl")!=null&&StringUtils.isNotEmpty(seriesmap.get("posterUrl").toString())){
				seriesmap.put("posterUrl", config.getPicture_url_prefix()+"/"+seriesmap.get("posterUrl"));
			}

			//处理合集
			processEpisodeList(seriesmap);
			return seriesmap;
		}

		return null;
	}
	private void	processEpisodeList(Map<String, Object> metadata) {
		if(metadata==null||metadata.isEmpty()){
			return ;
		}
		// 获取合集下每个分集中的最大媒体内容清晰度
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(metadata.get("contentId"));
		String sql ="SELECT max(m.definition) definition,pm.programContentId,m.duration "
				+ "FROM movie m "
				+ "LEFT JOIN program_movie pm ON pm.movieContentId=m.contentId "
				+ "LEFT JOIN series_program sp ON sp.programContentId=pm.programContentId "
				+ "where sp.seriesContentId=?  group by pm.programContentId";;
				List<Map<String, Object>> maxFileformatlist =this.jdbcTemplate.queryForList(sql,paramList.toArray());
				Map<String, Integer> maxFormatMap = new HashMap<String, Integer>();
				Map<String, String> lengthMap = new HashMap<String, String>();
				for (Map<String, Object> map : maxFileformatlist) {
					maxFormatMap.put((String) map.get("programContentId"),
							(Integer) map.get("definition"));
					lengthMap.put((String) map.get("programContentId"),
							(String) map.get("duration"));
				}

				// 获取合集下每个分集信息
				paramList.clear();
				paramList.add(metadata.get("contentId"));
				sql = 	 "SELECT p.contentId pContentId,p.name,p.subname,p.number 'index',p.awards,"
						+ "p.detailPicUrl thumbnailUrl,p.posterPicUrl posterUrl,p.type,p.periods "
						+ "FROM program p "
						+ "LEFT JOIN series_program sp ON sp.programContentId=p.contentId "
						+ "WHERE sp.seriesContentId=? and  p.status=1  "
						+ "ORDER BY p.number,p.type";
				List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,paramList.toArray());
				Set<Integer> sequenceSet = new HashSet<Integer>();
				// 剧集列表
				List<Object> episodeList = new ArrayList<Object>();
				metadata.put("episodeList", episodeList);
				// 片花列表
				List<Map<String, Object>> clipsList = new ArrayList<Map<String, Object>>();
				metadata.put("clipsList", clipsList);
				// 预告片列表
				List<Map<String, Object>> prevueList = new ArrayList<Map<String, Object>>();
				metadata.put("prevueList", prevueList);
				//String url=Utils.getEPGConfig(ConfigKey.PICTURE_SERVER_URL.toString()) ;
				for (Map<String, Object> map : list) {

					if(map.get("thumbnailUrl")!=null&&StringUtils.isNotEmpty(map.get("thumbnailUrl").toString())){
						map.put("thumbnailUrl", config.getPicture_url_prefix()+"/"+map.get("thumbnailUrl"));
					}
					if(map.get("posterUrl")!=null&&StringUtils.isNotEmpty(map.get("posterUrl").toString())){
						map.put("posterUrl", config.getPicture_url_prefix()+"/"+map.get("posterUrl"));
					}
					Integer number = (Integer) map.get("index");
					Integer type = (Integer) map.get("type");

					map.put("length", lengthMap.get(map.get("pContentId")));
					map.put("format", maxFormatMap.get(map.get("pContentId")));

					if (type==1) {
						episodeList.add(map);
						sequenceSet.add(number);
					} else if (type==2) {
						clipsList.add(map);
					} else if (type==3&& !sequenceSet.contains(number)) {
						prevueList.add(map);
					} // 有正片，则不给客户端返回预告片。由于sql查询语句已按isintact排序，正片在前。
					paramList.clear();
					paramList.add(map.get("pContentId"));
					String movessql = " "
							+ "SELECT m.contentId mContentId,m.definition format,m.is3d is3D,m.subtitles,"
							+ "m.duration length,concat(m.bitRateType,'') bitrate FROM movie m "
							+ "LEFT JOIN program_movie pm ON pm.movieContentId=m.contentId "
							+ "WHERE pm.programContentId=?  ORDER BY m.bitRateType desc";
					List<Map<String, Object>> movies =this.jdbcTemplate.queryForList(movessql,paramList.toArray());
					map.put("movies", movies);
					// 处理字幕subtitles
					for (Map<String, Object> m2 : movies){
						String subtitles = (String) m2.get("subtitles");
						// subtitles为json格式
						if (subtitles != null && subtitles.startsWith("[{")) {
							try {
								List<Map> list2 = JsonUtils.json2ObjList(subtitles,Map.class);
								m2.put("subtitles", list2);
							} catch (Exception e) {
								logger.error("subtitles", e);
							}
						} else {
							m2.put("subtitles", null);
						}
					}
				}
	}
}
