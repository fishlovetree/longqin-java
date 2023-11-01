package com.longqin.system.service;

import com.longqin.system.entity.Menu;

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
public interface IMenuService extends IService<Menu> {
	
	List<Menu> getMenuList();
	
	List<Map<String, Object>> getMenuTree();
	
	List<Menu> getUserMenuList(Integer userId, Integer organizationId);
	
	List<Map<String, Object>> getUserMenuTree(Integer userId, Integer organizationId);
	
	int addMenu(Menu menu) throws Exception;
	
	int editMenu(Menu menu) throws Exception;

	int deleteMenu(Integer menuId) throws Exception;
}
