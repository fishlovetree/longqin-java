package com.longqin.system.mapper;

import com.longqin.system.entity.Department;
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
public interface DepartmentMapper extends BaseMapper<Department> {

	Department selectById(@Param("id") Integer id);
	
	List<Department> selectList(@Param("organizationId") Integer organizationId);
	
	int updateStatus(@Param("id") Integer id);
	
	int selectChildrenCount(@Param("parentId") Integer parentId);
}
