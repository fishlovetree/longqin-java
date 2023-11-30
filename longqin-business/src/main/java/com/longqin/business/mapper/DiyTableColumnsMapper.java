package com.longqin.business.mapper;

import com.longqin.business.entity.DiyTableColumns;
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
 * @since 2023-11-09
 */
@Mapper
public interface DiyTableColumnsMapper extends BaseMapper<DiyTableColumns> {

	List<DiyTableColumns> selectTableColumns(@Param("tableId") Integer tableId);
	
	int deleteTableColumns(@Param("tableId") Integer tableId);
}
