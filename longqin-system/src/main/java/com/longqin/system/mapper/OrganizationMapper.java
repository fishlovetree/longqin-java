package com.longqin.system.mapper;

import com.longqin.system.entity.Organization;
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
public interface OrganizationMapper extends BaseMapper<Organization> {

	Organization selectById(@Param("id") Integer id);
	
	Organization selectByCode(@Param("code") String code);
	
	Organization selectByName(@Param("name") String name);
	
	int selectCountByName(@Param("name") String name, @Param("id") Integer id);
	
	List<Organization> selectList();
	
	List<Organization> selectPage(@Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount();
	
	int updateStatus(@Param("id") Integer id);
}
