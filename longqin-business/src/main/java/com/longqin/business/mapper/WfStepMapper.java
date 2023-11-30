package com.longqin.business.mapper;

import com.longqin.business.entity.WfStep;
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
 * @since 2023-11-01
 */
@Mapper
public interface WfStepMapper extends BaseMapper<WfStep> {

	List<WfStep> selectPage(@Param("workId")Integer workId, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("workId")Integer workId);
}
