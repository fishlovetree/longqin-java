package com.longqin.business.mapper;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.WfNode;
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
 * @since 2023-11-01
 */
@Mapper
public interface WfNodeMapper extends BaseMapper<WfNode> {

	int deleteByFlow(@Param("flowId")Integer flowId);
	
	List<WfNode> selectFlowNodes(@Param("flowId")Integer flowId);
	
	WfNode selectFlowBeginNode(@Param("flowId")Integer flowId);
	
	DesForm selectFlowBeginNodeForm(@Param("flowId")Integer flowId);
}
