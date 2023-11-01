package com.longqin.system.mapper;

import com.longqin.system.entity.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

	Role selectById(@Param("id") Integer id);
	
	List<Role> selectList(@Param("organizationId") Integer organizationId);
	
	List<Role> selectPage(@Param("organizationId") Integer organizationId, @Param("startIndex") Integer startIndex, 
			@Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId);
	
	int updateStatus(@Param("id") Integer id);
	
	List<Integer> selectRoleMenus(@Param("id") Integer id);
	
	int deleteRoleMenu(@Param("id") Integer id);
	
	int insertRoleMenu(@Param(value = "roleId") Integer roleId, @Param(value = "menus") List<Integer> menus);
}
