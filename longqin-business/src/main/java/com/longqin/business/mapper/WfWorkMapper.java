package com.longqin.business.mapper;

import com.longqin.business.entity.WfWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface WfWorkMapper extends BaseMapper<WfWork> {

	int closeWork(@Param("workId")Integer workId);
	
	int disableWork(@Param("workId")Integer workId);
	
	int getCreator(@Param("workId")Integer workId);
}
