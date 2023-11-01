package com.longqin.system.mapper;

import com.longqin.system.entity.Errorlog;
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
public interface ErrorlogMapper extends BaseMapper<Errorlog> {

	List<Errorlog> selectPage(@Param("beginDate") String beginDate,
			@Param("endDate") String endDate, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);
}
