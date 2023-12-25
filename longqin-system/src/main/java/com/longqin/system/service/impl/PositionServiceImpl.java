package com.longqin.system.service.impl;

import com.longqin.system.entity.Position;
import com.longqin.system.mapper.PositionMapper;
import com.longqin.system.service.IPositionService;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

	@Autowired
	private PositionMapper positionMapper;

	@Override
	public List<Position> getPositionList(Integer organizationId) {
		return positionMapper.selectList(organizationId);
	}

	@Override
	public List<Map<String, Object>> getPositionTree(Integer organizationId) {
		List<Position> positions = positionMapper.selectList(organizationId);
		return createTree(positions);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createTree(List<Position> positions) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < positions.size(); i++) {
			Map<String, Object> cmap = new HashMap<String, Object>();
			Position item = positions.get(i);
			cmap = setMap(cmap, item);
			cmap.put("state", "open");// 默认打开
			//cmap.put("children", new ArrayList<Map<String, Object>>());
			map.put(item.getPositionId(), cmap);
		}
		// 节点级联关系
		for (Map.Entry<Integer, Map<String, Object>> entry : map.entrySet()) {
			if (map.containsKey(entry.getValue().get("parentId"))) {
				Map<String, Object> parent = map.get(entry.getValue().get("parentId"));
				Object children = parent.get("children");
				if (children == null){
					parent.put("children", new ArrayList<Map<String, Object>>());
				}
				((List<Map<String, Object>>) parent.get("children")).add(entry.getValue());
			} else {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	private Map<String, Object> setMap(Map<String, Object> map, Position position) {
		map.put("id", position.getPositionId());
		map.put("positionId", position.getPositionId());
		map.put("parentId", position.getParentId());
		map.put("positionName", position.getPositionName());
		map.put("title", position.getPositionName());
		map.put("positionLevel", position.getPositionLevel());
		map.put("description", position.getDescription());
		map.put("organizationId", position.getOrganizationId());
		return map;
	}

	@OperationLog(title = "添加职位", content = "'职位名称：' + #position.getPositionName()", operationType = "0")
	@Override
	public int addPosition(Position position) throws Exception {
		return positionMapper.insert(position);
	}

	@OperationLog(title = "修改职位", content = "'职位名称：' + #position.getPositionName()", operationType = "2")
	@Override
	public int editPosition(Position position) throws Exception {
		return positionMapper.updateById(position);
	}

	@OperationLog(title = "删除职位", content = "'职位id：' + #positionId", operationType = "1")
	@Override
	public int deletePosition(Integer positionId) throws Exception {
		// 是否有子级，有则提示
		int count = positionMapper.selectChildrenCount(positionId);
		if (count > 0) {
			return -1;
		}
		return positionMapper.updateStatus(positionId);
	}
}
