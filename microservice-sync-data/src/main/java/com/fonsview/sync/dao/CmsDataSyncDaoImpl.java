/**
 * 
 */
package com.fonsview.sync.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;








import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fonsview.sync.utils.StringUtils;


/**
 * @Description 
 * @author hoob
 * @date 2018年10月29日下午8:13:30
 */
@Repository("cmsDataSyncDao")
public class CmsDataSyncDaoImpl implements CmsDataSyncDao{

	
	
	@Resource
	JdbcTemplate jdbcTemplate;



	
	/**
	 * @Title prcessCmsData
	 * @Description 对接统计系统，给统计系统提供数据
	 * @param 
	 * @return List<Map<String,Object>>
	 * @throws 
	 */
	public List<Map<String,Object>>prcessCmsData310(String msgType,String startTime,String endTime,int start,int num,String language){
		//根据msgTypep获取对应数据
		StringBuffer sql=new StringBuffer();
		ArrayList<Object>param =new ArrayList<Object>();
		if("GetUpdatedCategory".equals(msgType)){
			//获取更新的栏目数据
			sql.append("select DISTINCT c.contentId ,c.contentId as CategoryId,i18n.name as Name,c.createTime,c.updateTime,c.description as Description,c.parentContentId as ParentId,"
					+ " c.createTime as CreateDate,i18n.language  as Language,c.model as Model,c.sortNum as Sequence,i18n.picture as Picture,c.cpId as CpId "
					+ " from category c "
					+ " LEFT JOIN i18n_category i18n on c.contentId=i18n.contentId "
					+ " where c.recycle=0 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			if(StringUtils.isNotEmpty(language)){
				sql.append(" and i18n.language= ?");
				param.add(language);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		else if("GetDeletedCategory".equals(msgType)){
			sql.append("select DISTINCT c.contentId  from category c  where c.recycle=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
		
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSeries".equals(msgType)){
			sql.append("select DISTINCT s.contentId ,s.contentId as SeriesId,s.name as Name ,s.createTime,s.updateTime,s.createTime as CreateDate,ps.execTime as PublishTime,"
					+ " s.validTime as ValidTime ,s.invalidTime as InvalidTime,999 as Sequence,ps.status as Status,"
					+ " s.actors as Actors,s.awards as  Awards,s.directors as Directors ,s.intro as Intro,s.keyword as Keywords,s.vodType as ProgramType,"
					+ "s.cpId as CpId,s.rating as Rating,0 as Counts,s.totalNumber as TotalNum,s.updatedNumber as UpdateNum ,s.score as  Score "
					+ ",s.releaseYear as releaseYear,s.genre as Kind,s.areaCode as AreaCode,s.price as Price,s.detailPicUrl as DetailPicUrl,s.posterPicUrl as PosterPicUrl,s.tags as Tags  from series s "
					//+ " LEFT JOIN i18n_series i18n on s.contentId=i18n.contentId "
					+ " LEFT JOIN publish ps on s.contentId=ps.contentId and ps.mediaType=s.mediaType "
					+ " WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and s.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and s.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and s.recycle=0 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
	
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedSeries".equals(msgType)){
			//获取删除的合集数据
			sql.append("select DISTINCT s.contentId  from series s "
					+ " WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and s.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and s.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and s.recycle=1 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedProgram".equals(msgType)){
			 sql.append("select DISTINCT p.contentId ,p.contentId as ProgramId ,p.name as Name,p.createTime,p.updateTime,p.createTime as CreateDate,ps.execTime as PublishTime,"
			 		+ " '' as ValidTime,'' as InvalidTime,p.number as  Sequence,ps.status as Status,p.actors as Actors,p.awards as Awards,"
			 		+ "p.directors as Directors,p.intro as Intro,p.type as IsIntact,p.keyword as Keywords,p.vodType as ProgramType, p.seriesFlag as SeriesFlag,"
			 		+ " sp.seriesContentId as SeriesId,p.detailPicUrl as DetailPicUrl,p.posterPicUrl  as PosterPicUrl ,p.cpId as CpId,0 as Rating from program p left join series_program sp on p.contentId =sp.programContentId "
			 	//	+ " left join i18n_program i18n on p.contentId=i18n.contentId"
			 		+ " left join publish ps on ps.contentId=p.contentId and ps.mediaType=p.mediaType "
			 		+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and p.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and p.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and p.recycle=0 ");
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedProgram".equals(msgType)){
			       sql.append("select DISTINCT p.contentId  from program p "
					+ " WHERE  1=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and p.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and p.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" and p.recycle=1 ");
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedMedia".equals(msgType)){
			    sql.append("select m.contentId ,m.contentId as MediaId,m.createTime as CreateDate,m.name as Name ,m.createTime,m.updateTime,'' as PublishTime,'' as ValidTime ,'' as InvalidTime ,1 as Status,"
					+ "  m.audioFormat as AudioFormat,m.bitRateType as Bitrate,m.fileSize as FileSize,m.resolution as Resolution,m.definition as FileFormatDesc"
					+ " ,m.type as Flag,'' as Language,m.duration as  Length,m.playUrl as PlayUrl,''  as PreviewUrl,m.fileUrl  as DownloadUrl,pm.programContentId as ProgramId  "
					+ ", m.cpId as CpId,0 as Rating from movie m left join program_movie pm on m.contentId =pm.movieContentId "
					+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and m.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and m.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and m.recycle=0 ");
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedMedia".equals(msgType)){
		    sql.append("select m.contentId  from movie m "
		    		+ " WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and m.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and m.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and m.recycle=1 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedChannel".equals(msgType)){
			     sql.append("select c.contentId ,c.contentId as ChannelId,i18n.name as Name,c.createTime,c.updateTime,i18n.description as Description,c.cpCode as Code ,c.createTime as CreateDate,"
			     		+ " c.validTime as ValidTime,c.invalidTime as InvalidTime,ps.execTime as PublishTime,'' as ProtocolType,999 as Sequence,"
			     		+ " ps.status as Status ,i18n.subname as Subname,c.tags as Tags ,1 as Top,c.fileUrl as BitRateType,c.channelNumber as ChannelNum,"
			     		+ " c.channelType as ChannelType,i18n.language as DefaultLanguage,c.physicalChannelCode as PhysicalChannelCode,"
			     		+ " c.playUrl as PlayUrl,i18n.logoUrl as Poster,c.cpId as CpId,c.rating as Rating,au.status as CheckStatus  from channel c left join i18n_channel i18n on c.contentId=i18n.contentId "
			     		+ " left join audit au on au.contentId=c.contentId and au.mediaType=c.mediaType left join publish ps on ps.contentId=c.contentId and ps.mediaType=c.mediaType where recycle=0");
			        if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and c.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and c.updateTime<= ?");
						param.add(endTime);
					}
					if(StringUtils.isNotEmpty(language)){
						sql.append(" and i18n.language= ?");
						param.add(language);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSchedule".equals(msgType)){
			   sql.append("select DISTINCT s.contentId ,s.contentId as ScheduleId,c.contentId as ChannelId,c.contentId as channelContentId,s.name as Name ,s.createTime,s.updateTime,s.description as Description,"
			   		+ " s.cpCode as Code ,s.createTime as CreateDate,s.createTime ,'' as ValidTime,'' as InvalidTime ,'' as PublishTime,'' as ProtocolType,999 as Sequence,1 as Status,"
			   		+ "s.subname as Subname,s.tags as Tags ,1 as Top,s.startTime as StartTime,s.endTime as EndTime,s.picUrl as PicUrl ,s.cpId as CpId,s.date "
			   		+ "from schedulerecord s "
			   		//+ "left join i18n_schedulerecord i18n on s.contentId=i18n.contentId "
			   		+ "left join channel c on c.contentId =s.channelContentId "
					+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and s.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and s.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and s.recycle=0 ");
				sql.append(" limit ? ,?");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedSchedule".equals(msgType)){
			 sql.append("select DISTINCT s.contentId  from schedulerecord s "
						+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and s.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and s.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and s.recycle=1 ");
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeleteChannel".equals(msgType)){
			sql.append("select DISTINCT c.contentId   from channel c  where recycle=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime> ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		return null;
	}
	/**
	 * @Title getCategoryContentIds
	 * @Description 根据内容的contentId获取内容的栏目
	 * @param 
	 * @return List<String>
	 * @throws 
	 */
	public List<String>getCategoryContentIds310(String contentId,int mediaType){
		if(StringUtils.isEmpty(contentId)){
			return null;
		  }
			//合集
			StringBuilder sql=new StringBuilder();
			ArrayList<Object>params=new ArrayList<Object>(); 
			sql.append("select DISTINCT categoryContentId from category_association where objectMediaType=? and objectContentId=? and recycle=0");
			params.add(mediaType);
			params.add(contentId);
			return this.jdbcTemplate.queryForList(sql.toString(), params.toArray(),String.class);
	}
	/**
	 * @Title prcessCmsData300
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> prcessCmsData300(String msgType,
			String startTime, String endTime, int start, int num,
			String language) {
		//根据msgTypep获取对应数据
		StringBuffer sql=new StringBuffer();
		ArrayList<Object>param =new ArrayList<Object>();
		if("GetUpdatedCategory".equals(msgType)){
			sql.append("SELECT DISTINCT t1.contentId,t1.contentId CategoryId,t1.name Name,t1.description Description,c2.contentId ParentId,"
					+ "t1.createDate CreateDate,t1.createDate as createTime,t1.updateTime as updateTime,t1.language Language,t1.model Model,t1.modelName ModelName,t1.sequence Sequence,"
					+ "t1.picture Picture,cpr.cpId CpId "
					+ "FROM category t1 LEFT JOIN category c2 on c2.id=t1.parentId "
					+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
					+ "WHERE t1.recycle=0");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and t1.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and t1.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		else if("GetDeletedCategory".equals(msgType)){
			sql.append( "SELECT DISTINCT t1.contentId FROM category t1 LEFT JOIN category c2 on c2.id=t1.parentId "
					+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
					+ "WHERE t1.recycle=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and t1.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and t1.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSeries".equals(msgType)){
			sql.append(	"SELECT DISTINCT t1.contentId,t1.contentId SeriesId,t1.name Name,t1.createDate CreateDate,t1.createDate as createTime,t1.updateTime as updateTime,t1.publishTime PublishTime,"
					+ "t1.validTime ValidTime,t1.invalidTime InvalidTime,t1.sequence Sequence,t1.status Status,"
					+ "t1.actors Actors,t1.awards Awards,t1.directors Directors,t1.intro Intro,t1.keyword Keywords,"
					+ "t1.programType ProgramType,t1.detailPicUrl DetailPicUrl,t1.posterPicUrl PosterPicUrl,"
					+ "cpr.cpId CpId,t1.rating Rating,0 Counts,totalnum TotalNum,tags Tags,updatenum UpdateNum,"
					+ "score Score,releaseYear ReleaseYear,kind Kind,areaCode AreaCode,price Price "
					+ "FROM series t1 LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
					 + "WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and t1.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and t1.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and t1.recycle=0 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedSeries".equals(msgType)){
			//获取删除的合集数据
			sql.append("SELECT DISTINCT t1.contentId FROM series t1 LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
					 + "WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and t1.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and t1.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and t1.recycle=1 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedProgram".equals(msgType)){

			 sql.append("SELECT DISTINCT t1.contentId,t1.contentId ProgramId,t1.name Name,t1.createDate as createTime,t1.updateTime as updateTime,t1.createDate CreateDate,t1.publishTime PublishTime,"
						+ "t1.validTime ValidTime,t1.invalidTime InvalidTime,t1.sequence Sequence,t1.status Status,"
						+ "t1.actors Actors,t1.awards Awards,t1.directors Directors,t1.intro Intro,t1.isintact IsIntact,"
						+ "t1.keyword Keywords,t1.programType ProgramType,t1.seriesFlag SeriesFlag,sp.series_contentId SeriesId,"
						+ "t1.detailPicUrl DetailPicUrl,t1.posterPicUrl PosterPicUrl,cpr.cpId CpId,t1.rating Rating "
						+ "FROM program t1 LEFT JOIN series_program sp on sp.program_contentId=t1.contentId "
						+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
						 + "WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and t1.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and t1.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and t1.recycle=0 ");
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedProgram".equals(msgType)){
			       sql.append("SELECT DISTINCT t1.contentId FROM program t1 LEFT JOIN series_program sp on sp.program_contentId=t1.contentId "
			    		   + "WHERE  1=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and t1.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and t1.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" and t1.recycle=1 ");
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedMedia".equals(msgType)){
			    sql.append("SELECT DISTINCT t1.contentId,t1.contentId MediaId,t1.title as Name ,t1.createDate as createTime,t1.updateTime as updateTime,t1.createDate CreateDate,t1.publishTime PublishTime,"
						+ "t1.validTime ValidTime,t1.invalidTime InvalidTime,t1.status Status,t1.audioFormat AudioFormat,"
						+ "t1.bitrate Bitrate,t1.fileSize FileSize,t1.resolution Resolution,t1.fileformatdesc FileFormatDesc,"
						+ "t1.flag Flag,t1.language Language,t1.length Length,t1.playUrl PlayUrl,t1.previewUrl PreviewUrl,"
						+ "t1.downloadUrl DownloadUrl,pm.program_contentId ProgramId,cpr.cpId CpId,t1.rating Rating "
						+ "FROM mediacontent t1 LEFT JOIN program_mediacontent pm on pm.mediacontent_contentId=t1.contentId "
						+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
						+ "WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and t1.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and t1.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" and t1.recycle=0 ");
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedMedia".equals(msgType)){
		    sql.append("SELECT DISTINCT t1.contentId FROM mediacontent t1 "
		    		+ "WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and t1.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and t1.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and t1.recycle=1 ");
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedChannel".equals(msgType)){
	
			     sql.append("SELECT DISTINCT t1.contentId,t1.contentId ChannelId,t1.Name,t1.createDate as createTime,t1.updateTime as updateTime,t1.Description,t1.Code,"
			 			+ "t1.CreateDate,t1.ValidTime,t1.InvalidTime,t1.PublishTime,t1.ProtocolType,"
						+ "t1.Sequence,t1.Status,t1.Subname,t1.Tags,t1.Top,t1.BitRateType,"
						+ "t1.channelNumber ChannelNum,t1.ChannelType,t1.DefaultLanguage,"
						+ "t1.PhysicalChannelCode,t1.playUrl,t1.poster,t1.contentId,cpr.cpId CpId,"
						+ "t1.rating Rating,t1.CheckStatus "
						+ "FROM channel t1 LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
						+ "WHERE recycle=0 ");
			        if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and t1.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and t1.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSchedule".equals(msgType)){
			   sql.append("SELECT DISTINCT t1.contentId ScheduleId,t1.contentId,t1.channelContentId ChannelId,t1.channelContentId as channelContentId,t1.name as Name,"
			   		+ " t1.createDate as createTime,t1.updateTime as updateTime,t1.Description,t1.Code,"
						+ "t1.CheckStatus,t1.CreateDate,t1.ValidTime,t1.InvalidTime,t1.PublishTime,t1.ProtocolType,"
						+ "t1.Sequence,t1.Status,t1.Subname,t1.Tags,t1.Top,t1.StartTime,t1.EndTime,t1.DefaultLanguage,"
						+ "t1.picUrl PicUrl,cpr.cpId CpId ,t1.date FROM schedulerecord t1 "
					//	+ "LEFT JOIN channel c on c.id=t1.channel_id "
						+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
						+ "WHERE 1=1 ");
			   if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and (t1.updateTime>= ? or t1.createDate>=?)");
					param.add(startTime);
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and t1.updateTime<= ? or t1.createDate<=?");
					param.add(endTime);
					param.add(endTime);
				}
				sql.append("  and t1.recycle=0 ");
				sql.append(" limit ?,?");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedSchedule".equals(msgType)){
			 sql.append("SELECT DISTINCT t1.contentId  FROM schedulerecord t1 "
					//	+ "LEFT JOIN channel c on c.id=t1.channel_id "
					//	+ "LEFT JOIN contentprovider cpr ON cpr.id=t1.cp_id "
						+ "WHERE 1=1  ");
				   if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and t1.updateTime>= ? or t1.createDate>=? ");
						param.add(startTime);
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and t1.updateTime<= ? or t1.createDate<=?");
						param.add(endTime);
						param.add(endTime);
					}
					sql.append(" and t1.recycle=1 ");
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeleteChannel".equals(msgType)){
			sql.append("select DISTINCT c.contentId   from channel c  where 1=1   ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime> ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" and recycle=1 ");
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		return null;
	}
	/**
	 * @Title getCategoryContentIds300
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<String> getCategoryContentIds300(String contentId, int mediaType) {
		if(StringUtils.isEmpty(contentId)){
			return null;
		  }
			//合集
			StringBuilder sql=new StringBuilder();
			ArrayList<Object>params=new ArrayList<Object>(); 
			sql.append("select DISTINCT category_contentId as  categoryContentId from minimetadata_category where contentType=? and contentId=? and recycle=0");
			params.add(mediaType);
			params.add(contentId);
			return this.jdbcTemplate.queryForList(sql.toString(), params.toArray(),String.class);
		
	}
	/**
	 * @Title prcessCmsData310ForDass
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> prcessCmsData310ForDass(String msgType,
			String startTime, String endTime, int start, int num,
			String language) {
		//根据msgTypep获取对应数据
		StringBuffer sql=new StringBuffer();
		ArrayList<Object>param =new ArrayList<Object>();
		if("GetUpdatedCategory".equals(msgType)){
			//获取更新的栏目数据
			sql.append("select DISTINCT c.contentId,c.cpCode as code,c.recycle ,c.name,c.createTime,c.updateTime from category c "
					+ " where 1=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		else if("GetDeletedCategory".equals(msgType)){
			sql.append("select DISTINCT c.contentId  from category c  where c.recycle=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSeries".equals(msgType)){
			sql.append("select DISTINCT s.contentId,s.name as name ,s.cpCode as code,s.recycle,s.createTime,s.updateTime,ps.status,ps.execTime as publishTime,s.vodType as programType  from series s left join publish ps on s.contentId=ps.contentId and s.mediaType=ps.mediaType "
					+ " WHERE  1=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and s.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and s.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedSeries".equals(msgType)){
			//获取删除的合集数据
			sql.append("select DISTINCT s.contentId  from series s "
					+ " WHERE  s.recycle=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and s.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and s.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedProgram".equals(msgType)){
			 sql.append("select DISTINCT p.contentId ,p.cpCode as code ,p.recycle,p.name as name ,p.createTime,p.updateTime from program p "
			 		+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and p.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and p.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedProgram".equals(msgType)){
			       sql.append("select DISTINCT p.contentId  from program p "
					+ " WHERE  p.recycle=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and p.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and p.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedMedia".equals(msgType)){
			    sql.append("select m.contentId ,m.name as name,m.cpCode as code,m.recycle ,m.createTime,m.updateTime,m.definition as fileformatdesc,m.duration as length from movie m "
					+ " WHERE  1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and m.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and m.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			
		}else if("GetDeletedMedia".equals(msgType)){
		    sql.append("select m.contentId  from movie m "
		    		+ " WHERE m.recycle=1 ");
		    if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and m.updateTime>= ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and m.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,? ");
			param.add(start);
			param.add(num);
		    return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedChannel".equals(msgType)){
			     sql.append("select c.contentId ,c.name as name ,c.createTime,c.updateTime,c.cpCode as code ,c.recycle,c.liveType as liveType from channel c where 1=1 ");
			        if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and c.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and c.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetUpdatedSchedule".equals(msgType)){
			   sql.append("select DISTINCT s.contentId ,s.cpCode as code,s.recycle,s.name as name ,s.createTime,s.updateTime,s.startTime as startTime,s.endTime as endTime,s.date,s.channelContentId "
			   		+ " from schedulerecord s "
					+ " WHERE 1=1 ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and s.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and s.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
				return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeletedSchedule".equals(msgType)){
			 sql.append("select DISTINCT s.contentId  from schedulerecord s "
						+ " WHERE  s.recycle=1  ");
			    if(StringUtils.isNotEmpty(startTime)){
					sql.append(" and s.updateTime>= ?");
					param.add(startTime);
				}
				if(StringUtils.isNotEmpty(endTime)){
					sql.append(" and s.updateTime<= ?");
					param.add(endTime);
				}
				sql.append(" limit ?,? ");
				param.add(start);
				param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}else if("GetDeleteChannel".equals(msgType)){
			sql.append("select DISTINCT c.contentId   from channel c  where recycle=1 ");
			if(StringUtils.isNotEmpty(startTime)){
				sql.append(" and c.updateTime> ?");
				param.add(startTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and c.updateTime<= ?");
				param.add(endTime);
			}
			sql.append(" limit ?,?");
			param.add(start);
			param.add(num);
			return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
		}
		return null;
	}
	/**
	 * @Title prcessCmsData300ForDass
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> prcessCmsData300ForDass(String msgType,
			String startTime, String endTime, int start, int num,
			String language) {
		//根据msgTypep获取对应数据
				StringBuffer sql=new StringBuffer();
				ArrayList<Object>param =new ArrayList<Object>();
				if("GetUpdatedCategory".equals(msgType)){
					//获取更新的栏目数据
					sql.append("select DISTINCT c.contentId,c.code,c.recycle ,c.name,c.createDate as createTime,c.updateTime from category c "
							+ " where 1=1 ");
					if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and c.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and c.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}
				else if("GetDeletedCategory".equals(msgType)){
					sql.append("select DISTINCT c.contentId  from category c  where c.recycle=1 ");
					if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and c.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and c.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetUpdatedSeries".equals(msgType)){
					sql.append("select DISTINCT s.contentId,s.code,s.recycle,s.name as name ,s.createDate as createTime,s.updateTime,s.status,s.programType,s.publishTime  from series s "
							+ " WHERE  1=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and s.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and s.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
					
				}else if("GetDeletedSeries".equals(msgType)){
					//获取删除的合集数据
					sql.append("select DISTINCT s.contentId  from series s "
							+ " WHERE  s.recycle=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and s.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and s.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetUpdatedProgram".equals(msgType)){
					 sql.append("select DISTINCT p.contentId ,p.name as name,p.code,p.recycle,p.createDate as createTime,p.updateTime "
					 		+ " from program p WHERE  1=1 ");
					    if(StringUtils.isNotEmpty(startTime)){
							sql.append(" and p.updateTime>= ?");
							param.add(startTime);
						}
						if(StringUtils.isNotEmpty(endTime)){
							sql.append(" and p.updateTime<= ?");
							param.add(endTime);
						}
						sql.append(" limit ?,? ");
						param.add(start);
						param.add(num);
						return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetDeletedProgram".equals(msgType)){
					       sql.append("select DISTINCT p.contentId  from program p "
							+ " WHERE  p.recycle=1 ");
						    if(StringUtils.isNotEmpty(startTime)){
								sql.append(" and p.updateTime>= ?");
								param.add(startTime);
							}
							if(StringUtils.isNotEmpty(endTime)){
								sql.append(" and p.updateTime<= ?");
								param.add(endTime);
							}
							sql.append(" limit ?,? ");
							param.add(start);
							param.add(num);
							return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetUpdatedMedia".equals(msgType)){
					    sql.append("select m.contentId,m.title as name,m.code,m.recycle ,m.createDate as createTime,m.updateTime,m.length,m.fileformatdesc from mediacontent m "
							+ " WHERE  1=1 ");
					    if(StringUtils.isNotEmpty(startTime)){
							sql.append(" and m.updateTime>= ?");
							param.add(startTime);
						}
						if(StringUtils.isNotEmpty(endTime)){
							sql.append(" and m.updateTime<= ?");
							param.add(endTime);
						}
						sql.append(" limit ?,? ");
						param.add(start);
						param.add(num);
						return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
					
				}else if("GetDeletedMedia".equals(msgType)){
				    sql.append("select m.contentId  from mediacontent m "
				    		+ " WHERE m.recycle=1 ");
				    if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and m.updateTime>= ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and m.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,? ");
					param.add(start);
					param.add(num);
				    return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetUpdatedChannel".equals(msgType)){
					     sql.append("select c.contentId ,c.name as name,c.code,c.recycle ,c.createDate as createTime,c.updateTime,c.liveType from channel c where 1=1 ");
					        if(StringUtils.isNotEmpty(startTime)){
								sql.append(" and c.updateTime>= ?");
								param.add(startTime);
							}
							if(StringUtils.isNotEmpty(endTime)){
								sql.append(" and c.updateTime<= ?");
								param.add(endTime);
							}
							sql.append(" limit ?,?");
							param.add(start);
							param.add(num);
							return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetUpdatedSchedule".equals(msgType)){
					   sql.append("select DISTINCT s.contentId,s.name as name,s.code,s.recycle,s.createDate as createTime,s.updateTime,s.startTime as startTime,s.endTime as endTime,s.date,s.channelContentId "
					   		+ " from schedulerecord s "
							+ " WHERE 1=1 ");
					    if(StringUtils.isNotEmpty(startTime)){
							sql.append(" and s.createDate>= ?");
							param.add(startTime);
						}
						if(StringUtils.isNotEmpty(endTime)){
							sql.append(" and s.createDate<= ?");
							param.add(endTime);
						}
						sql.append(" limit ?,? ");
						param.add(start);
						param.add(num);
						return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetDeletedSchedule".equals(msgType)){
					 sql.append("select DISTINCT s.contentId  from schedulerecord s "
								+ " WHERE  s.recycle=1  ");
					    if(StringUtils.isNotEmpty(startTime)){
							sql.append(" and s.createDate>= ?");
							param.add(startTime);
						}
						if(StringUtils.isNotEmpty(endTime)){
							sql.append(" and s.createDate<= ?");
							param.add(endTime);
						}
						sql.append(" limit ?,? ");
						param.add(start);
						param.add(num);
							return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}else if("GetDeleteChannel".equals(msgType)){
					sql.append("select DISTINCT c.contentId   from channel c  where recycle=1 ");
					if(StringUtils.isNotEmpty(startTime)){
						sql.append(" and c.updateTime> ?");
						param.add(startTime);
					}
					if(StringUtils.isNotEmpty(endTime)){
						sql.append(" and c.updateTime<= ?");
						param.add(endTime);
					}
					sql.append(" limit ?,?");
					param.add(start);
					param.add(num);
					return this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
				}
				return null;
	}
	/**
	 * @Title getProgramContentId
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public String getProgramContentId(String movieConentId, String version) {
		if("310".equals(version)){
			String sql="select programContentId from program_movie where movieContentId=?";
			List<Object>param=new ArrayList<Object>();
			param.add(movieConentId);
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			if(list!=null&&!list.isEmpty()){
				return list.get(0).get("programContentId").toString();
			}
		}else{
			String sql="select program_contentId from program_mediacontent where mediacontent_contentId=?";
			List<Object>param=new ArrayList<Object>();
			param.add(movieConentId);
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			if(list!=null&&!list.isEmpty()){
				return list.get(0).get("program_contentId").toString();
			}
		}
		return null;
	}
	/**
	 * @Title getSeriesContentId
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public String getSeriesContentId(String movieConentId, String version) {
		if("310".equals(version)){
			String sql="select seriesContentId from series_program where programContentId=?";
			List<Object>param=new ArrayList<Object>();
			param.add(movieConentId);
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			if(list!=null&&!list.isEmpty()){
				return list.get(0).get("seriesContentId").toString();
			}
		}else{
			String sql="select series_contentId from series_program where program_contentId=?";
			List<Object>param=new ArrayList<Object>();
			param.add(movieConentId);
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql.toString(),param.toArray());
			if(list!=null&&!list.isEmpty()){
				return list.get(0).get("series_contentId").toString();
			}
		}
		return null;
	}
	/**
	 * @Title getCmsDataForUDC
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<Map<String,Object>> getCmsDataForUDC300(String startTime,
			String endTime, int begin, int pageSize, int mediaType) {
		if(mediaType==1){
			String	sql="select name,contentId,detailPicUrl from program where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			//获取分集相关信息
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());

		}else if(mediaType==3){
			//获取直播相关信息
			String sql="select name,contentId,poster as logoUrl from channel where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());
		}
		else {
			//默认获取合集相关信息
			String	sql="select name,contentId,detailPicUrl,programType as seriesFlag,programType,totalnum as totalNumber,nownum as nowNumber  from series where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());
		}
	}
	/**
	 * @Title getCmsDataForUDC310
	 * @Description 
	 * @param 
	 * @return CmsDataSyncDao
	 * @throws 
	 */
	@Override
	public List<Map<String, Object>> getCmsDataForUDC310(String startTime,
			String endTime, int begin, int pageSize, int mediaType) {
		if(mediaType==1){
			String	sql="select name,contentId,detailPicUrl as detailPicUrl from program where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			//获取分集相关信息
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());

		}else if(mediaType==3){
			//获取直播相关信息
			String sql="select name,contentId,logoUrl as logoUrl from channel where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());
		}
		else {
			//默认获取合集相关信息
			String	sql="select name,contentId,detailPicUrl,seriesFlag,vodType,totalNumber,nowNumber  from series where updateTime>=? and updateTime <? order by id asc LIMIT ?,?";
			List<Object>param=new ArrayList<Object>();
			param.add(startTime);
			param.add(endTime);
			param.add(begin);
			param.add(pageSize);
			return this.jdbcTemplate.queryForList(sql,param.toArray());
		}
	}
}
