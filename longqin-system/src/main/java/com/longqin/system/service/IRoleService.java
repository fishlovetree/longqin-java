package com.longqin.system.service;

import com.longqin.system.entity.Role;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
public interface IRoleService extends IService<Role> {

	Role getById(int id);
	
	List<Role> getList(int organizationId);
	
	List<Role> getPage(int organizationId, int startIndex, int pageSize);
	
	int getCount(int organizationId);
	
	int delete(int id) throws Exception;
	
	int insert(Role entity) throws Exception;
	
	int update(Role entity) throws Exception;
	
	List<Integer> getMenus(int roleId);
	
	int setMenu(int roleId, String menuIds) throws Exception;
}
