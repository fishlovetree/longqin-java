package com.longqin.business.mapper;

import com.longqin.business.entity.DiyTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

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
public interface DiyTableMapper extends BaseMapper<DiyTable> {

	List<DiyTable> selectPage(@Param("organizationId") Integer organizationId, @Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("organizationId") Integer organizationId);
	
	int delete(@Param("id") Integer id);
	
	List<Map<String, String>> selectTableData(@Param("dataSql") String dataSql);
	
	int selectTableDataCount(@Param("countSql") String countSql);
}
