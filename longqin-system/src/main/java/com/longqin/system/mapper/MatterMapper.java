package com.longqin.system.mapper;

import com.longqin.system.entity.Matter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 事项 Mapper 接口
 * </p>
 *
 * @author longqin
 * @since 2024-09-04
 */
@Mapper
public interface MatterMapper extends BaseMapper<Matter> {
	
	List<Matter> selectByDate(@Param("date") String date, @Param("userId") Integer userId);

	int updateStatus(@Param("id") Integer id);
}
