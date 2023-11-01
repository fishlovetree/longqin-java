package com.longqin.system.mapper;

import com.longqin.system.entity.User;
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
public interface UserMapper extends BaseMapper<User> {

	User selectById(@Param("id") Integer id);
	
	User selectByName(@Param("userName") String userName);
	
	int selectCountByName(@Param("userName") String userName, @Param("id") Integer id);
	
	List<User> selectPage(@Param("organizationId") Integer organizationId, @Param("departmentId") String departmentId,
			@Param("nickName") String nickName, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId, @Param("departmentId") String departmentId,
			@Param("nickName") String nickName);
	
	int updateStatus(@Param("id") Integer id);
	
	int updatePassword(@Param("pwd") String pwd, @Param("id") Integer id);
	
	List<Integer> selectUserRoles(@Param("id") Integer id);
	
	int deleteUserRole(@Param("id") Integer id);
	
	int insertUserRole(@Param(value = "userId") Integer userId, @Param(value = "roles") List<Integer> roles);
	
	int selectUserPositionLevel(@Param("id") Integer id);
	
	List<Integer> selectUserIdByDeptAndPosition(@Param("departmentId") Integer departmentId, @Param("positionId") Integer positionId);
}
