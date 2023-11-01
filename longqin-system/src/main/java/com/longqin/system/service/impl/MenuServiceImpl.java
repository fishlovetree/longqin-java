package com.longqin.system.service.impl;

import com.longqin.system.entity.Menu;
import com.longqin.system.mapper.MenuMapper;
import com.longqin.system.service.IMenuService;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public List<Menu> getMenuList() {
		return menuMapper.selectList();
	}

	@Override
	public List<Map<String, Object>> getMenuTree() {
		List<Menu> menus = menuMapper.selectList();
		return createTree(menus);
	}

	@Override
	public List<Menu> getUserMenuList(Integer userId, Integer organizationId) {
		return menuMapper.selectUserMenuList(userId, organizationId);
	}

	@Override
	public List<Map<String, Object>> getUserMenuTree(Integer userId, Integer organizationId) {
		List<Menu> menus = menuMapper.selectUserMenuList(userId, organizationId);
		return createTree(menus);
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createTree(List<Menu> menus) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < menus.size(); i++) {
			Map<String, Object> cmap = new HashMap<String, Object>();
			Menu item = menus.get(i);
			cmap = setMap(cmap, item);
			cmap.put("state", "open");// 默认打开
			cmap.put("children", new ArrayList<Map<String, Object>>());
			map.put(item.getMenuId(), cmap);
		}
		// 节点级联关系
		for (Map.Entry<Integer, Map<String, Object>> entry : map.entrySet()) {
			if (map.containsKey(entry.getValue().get("parentId"))) {
				Map<String, Object> parent = map.get(entry.getValue().get("parentId"));
				((List<Map<String, Object>>) parent.get("children")).add(entry.getValue());
			} else {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	private Map<String, Object> setMap(Map<String, Object> map, Menu menu) {
		map.put("id", menu.getMenuId()); // id
		map.put("menuId", menu.getMenuId()); // id
		map.put("parentId", menu.getParentId());// 上级菜单id
		map.put("menuName", menu.getMenuName());// 菜单名称
		map.put("text", menu.getMenuName());// 菜单名称
		map.put("menuUrl", menu.getMenuUrl());// 菜单链接
		map.put("menuIcon", menu.getMenuIcon());// 菜单图标
		map.put("menuOrder", menu.getGroupSeq());// 菜单排序
		return map;
	}

	@OperationLog(title = "添加菜单", content = "'菜单名称：' + #menu.getMenuName()", operationType = "0")
	@Override
	public int addMenu(Menu menu) throws Exception {
		int count = menuMapper.selectCountByName(menu.getMenuName(), 0);
		if (count > 0) {
			return -2;
		}
		return menuMapper.insert(menu);
	}

	@OperationLog(title = "修改菜单", content = "'菜单名称：' + #menu.getMenuName()", operationType = "2")
	@Override
	public int editMenu(Menu menu) throws Exception {
		int count = menuMapper.selectCountByName(menu.getMenuName(), menu.getMenuId());
		if (count > 0) {
			return -2;
		}
		return menuMapper.updateById(menu);
	}

	@OperationLog(title = "删除菜单", content = "'菜单id：' + #menuId", operationType = "1")
	@Override
	public int deleteMenu(Integer menuId) throws Exception {
		// 是否有下级菜单，有则提示
		int count = menuMapper.selectChildrenCount(menuId);
		if (count > 0) {
			return -1;
		}
		return menuMapper.updateStatus(menuId);
	}
}
