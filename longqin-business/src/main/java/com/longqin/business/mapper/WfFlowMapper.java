package com.longqin.business.mapper;

import com.longqin.business.entity.WfFlow;
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
public interface WfFlowMapper extends BaseMapper<WfFlow> {

	List<WfFlow> selectPage(@Param("organizationId") Integer organizationId, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId);
	
	int deleteFlow(@Param("id") Integer id);
}
