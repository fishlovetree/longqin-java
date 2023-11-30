package com.longqin.business.mapper;

import com.longqin.business.entity.WfWorkform;
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
 * @since 2023-11-01
 */
@Mapper
public interface WfWorkformMapper extends BaseMapper<WfWorkform> {

	WfWorkform selectByProcessId(@Param("processId")Integer processId);
	
	List<WfWorkform> selectWorkFormList(@Param("workId")Integer workId);
	
	Map<String, String> selectTableDatas(@Param("tableName") String tableName, @Param("processId") Integer processId);
}
