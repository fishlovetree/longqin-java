package com.longqin.system.service;

import com.longqin.system.entity.Position;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
public interface IPositionService extends IService<Position> {

	List<Position> getPositionList(Integer organizationId);
	
	List<Map<String, Object>> getPositionTree(Integer organizationId);
	
	int addPosition(Position position) throws Exception;
	
	int editPosition(Position position) throws Exception;
	
	int deletePosition(Integer positionId) throws Exception;
}
