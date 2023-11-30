package com.longqin.business.mapper;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.WfProcess;
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
public interface WfProcessMapper extends BaseMapper<WfProcess> {

	WfProcess selectNodeFirstProcess(@Param("workId")Integer workId, @Param("nodeId")Integer nodeId);
	
	WfProcess selectProcessById(@Param("processId")Integer processId);
	
	int selectNodeProcessCount(@Param("workId")Integer workId, @Param("nodeId")Integer nodeId);
	
	int selectPreNodeProcessCount(@Param("workId")Integer workId, @Param("nodeId")Integer nodeId);
	
	int closeNodeProcess(@Param("workId")Integer workId, @Param("nodeId")Integer nodeId);
	
	int closeProcessById(@Param("processId")Integer processId);
	
	int disableWorkProcess(@Param("workId")Integer workId);
	
	List<WfProcess> selectPage(@Param("userId")Integer userId, @Param("status")Integer status,
			@Param("beginDate")String beginDate, @Param("endDate")String endDate,
			@Param("startIndex") Integer startIndex, @Param("size") Integer size);
	
	int selectCount(@Param("userId")Integer userId, @Param("status")Integer status,
			@Param("beginDate")String beginDate, @Param("endDate")String endDate);
	
	DesForm selectProcessForm(@Param("processId")Integer processId);
	
	Map<String, String> selectProcessFormData(@Param("processId")Integer processId, @Param("tableName")String tableName);
	
	int disableProcess(@Param("workId")Integer workId);
}
