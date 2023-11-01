package com.longqin.system.service;

import com.longqin.system.entity.Department;

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
public interface IDepartmentService extends IService<Department> {

	List<Department> getDepartmentList(Integer organizationId);
	
	List<Map<String, Object>> getDepartmentTree(Integer organizationId);
	
	int addDepartment(Department department) throws Exception;
	
	int editDepartment(Department department) throws Exception;
	
	int deleteDepartment(Integer departmentId) throws Exception;
}
