package com.longqin.business.mapper;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.entity.DesFormData;
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
 * @since 2023-10-31
 */
@Mapper
public interface DesFormMapper extends BaseMapper<DesForm> {

	int createFormTable(@Param("tableName") String tableName, @Param("columns") String columns,
			@Param("comment") String comment);
	
	int selectCountByTableName(@Param("tableName") String tableName, @Param("id") Integer id);
	
	int selectTableCount(@Param("tableName") String tableName);
	
	int renameTable(@Param("oldName") String oldName, @Param("newName") String newName);
	
	int deleteForm(@Param("id") Integer id);
	
	List<DesForm> selectPage(@Param("organizationId") Integer organizationId, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId);
	
	int insertFormData(DesFormData record);
	
	int updateFormData(@Param("tableName") String tableName, @Param("data") String data, @Param("id") Integer id);
	
	List<DesFormColumn> selectTableColumns(@Param("tableName") String tableName);
}
