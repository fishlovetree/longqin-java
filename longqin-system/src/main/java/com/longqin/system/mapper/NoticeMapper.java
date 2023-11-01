package com.longqin.system.mapper;

import com.longqin.system.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

	List<Notice> selectPage(@Param("organizationId") Integer organizationId, @Param("title") String title, @Param("beginDate") String beginDate,
			@Param("endDate") String endDate, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId, @Param("title") String title, @Param("beginDate") String beginDate,
			@Param("endDate") String endDate);
	
	int updateStatus(@Param("id") Integer id);
	
	List<String> selectNoticeFiles(@Param("id") Integer id);
	
	int deleteNoticeFiles(@Param("id") Integer id);
	
	int insertNoticeFiles(@Param(value = "noticeId") Integer noticeId, @Param(value = "filePaths") List<String> filePaths);
}
