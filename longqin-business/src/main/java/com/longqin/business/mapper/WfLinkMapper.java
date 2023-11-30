package com.longqin.business.mapper;

import com.longqin.business.entity.WfLink;
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
public interface WfLinkMapper extends BaseMapper<WfLink> {

    int deleteByFlow(@Param("flowId")Integer flowId);
	
	List<WfLink> selectFlowLinks(@Param("flowId")Integer flowId);
	
	List<WfLink> selectNodeLinks(@Param("nodeId")Integer nodeId);
}
