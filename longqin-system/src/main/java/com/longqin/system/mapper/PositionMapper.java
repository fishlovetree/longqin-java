package com.longqin.system.mapper;

import com.longqin.system.entity.Position;
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
public interface PositionMapper extends BaseMapper<Position> {

	Position selectById(@Param("id") Integer id);
	
	List<Position> selectList(@Param("organizationId") Integer organizationId);
	
	int updateStatus(@Param("id") Integer id);
	
	int selectChildrenCount(@Param("parentId") Integer parentId);
}
