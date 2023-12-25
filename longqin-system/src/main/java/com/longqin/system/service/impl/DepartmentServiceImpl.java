package com.longqin.system.service.impl;

import com.longqin.system.entity.Department;
import com.longqin.system.mapper.DepartmentMapper;
import com.longqin.system.service.IDepartmentService;
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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public List<Department> getDepartmentList(Integer organizationId) {
		return departmentMapper.selectList(organizationId);
	}

	@Override
	public List<Map<String, Object>> getDepartmentTree(Integer organizationId) {
		List<Department> departments = departmentMapper.selectList(organizationId);
		return createTree(departments);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createTree(List<Department> departments) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < departments.size(); i++) {
			Map<String, Object> cmap = new HashMap<String, Object>();
			Department item = departments.get(i);
			cmap = setMap(cmap, item);
			cmap.put("state", "open");// 默认打开
			//cmap.put("children", new ArrayList<Map<String, Object>>());
			map.put(item.getDepartmentId(), cmap);
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

	private Map<String, Object> setMap(Map<String, Object> map, Department department) {
		map.put("id", department.getDepartmentId());
		map.put("departmentId", department.getDepartmentId());
		map.put("parentId", department.getParentId());
		map.put("departmentName", department.getDepartmentName());
		map.put("title", department.getDepartmentName());
		map.put("description", department.getDescription());
		map.put("organizationId", department.getOrganizationId());
		return map;
	}

	@OperationLog(title = "添加部门", content = "'部门名称：' + #department.getDepartmentName()", operationType = "0")
	@Override
	public int addDepartment(Department department) throws Exception {
		return departmentMapper.insert(department);
	}

	@OperationLog(title = "修改部门", content = "'部门名称：' + #department.getDepartmentName()", operationType = "2")
	@Override
	public int editDepartment(Department department) throws Exception {
		return departmentMapper.updateById(department);
	}

	@OperationLog(title = "删除部门", content = "'部门id：' + #departmentId", operationType = "1")
	@Override
	public int deleteDepartment(Integer departmentId) throws Exception {
		// 是否有子级，有则提示
		int count = departmentMapper.selectChildrenCount(departmentId);
		if (count > 0) {
			return -1;
		}
		return departmentMapper.updateStatus(departmentId);
	}
}
