package com.longqin.system.service.impl;

import com.longqin.system.entity.Role;
import com.longqin.system.mapper.RoleMapper;
import com.longqin.system.service.IRoleService;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	@Autowired
	RoleMapper roleMapper;
	
	@Override
    public Role getById(int id) {
        return roleMapper.selectById(id);
    }
    
	@Override
    public List<Role> getList(int organizationId) {
        return roleMapper.selectList(organizationId);
    }

	@Override
    public List<Role> getPage(int organizationId, int startIndex, int pageSize) {
        return roleMapper.selectPage(organizationId, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId) {
        return roleMapper.selectCount(organizationId);
    }

	@OperationLog(title = "删除角色", content = "'角色id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
        return roleMapper.updateStatus(id);
    }

	@OperationLog(title = "添加角色", content = "'角色名称：' + #entity.getRoleName()", operationType = "0")
	@Override
    public int insert(Role entity) throws Exception {
        return roleMapper.insert(entity);
    }

	@OperationLog(title = "修改角色", content = "'角色名称：' + #entity.getRoleName()", operationType = "2")
	@Override
    public int update(Role entity) throws Exception {
        return roleMapper.updateById(entity);
    }

	@Override
    public List<Integer> getMenus(int roleId) {
        return roleMapper.selectRoleMenus(roleId);
    }

	@OperationLog(title = "设置角色菜单", content = "'角色id：' + #roleId + ', 菜单id：' + #menuIds", operationType = "7")
	@Override
    public int setMenu(int roleId, String menuIds) throws Exception {
    	int result = roleMapper.deleteRoleMenu(roleId);
		if (menuIds != null && menuIds.length() > 0) {
			List<Integer> menuIdArray = Arrays.asList(menuIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		    result = roleMapper.insertRoleMenu(roleId, menuIdArray);
		}
		return result;
    }
}
