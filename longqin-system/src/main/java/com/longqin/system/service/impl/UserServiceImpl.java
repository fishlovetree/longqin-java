package com.longqin.system.service.impl;

import com.longqin.system.entity.User;
import com.longqin.system.mapper.UserMapper;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.MD5Util;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	
	@Autowired
	UserMapper userMapper;

	@Override
    public User getById(int id)
    {
        return userMapper.selectById(id);
    }

	@Override
    public User getByName(String name)
    {
        return userMapper.selectByName(name);
    }

	@Override
    public int getCountByName(String name, int id)
    {
        return userMapper.selectCountByName(name, id);
    }

	@Override
    public List<User> getPage(int organizationId, String departmentId, String nickName, int startIndex, int pageSize)
    {
        return userMapper.selectPage(organizationId, departmentId, nickName, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId, String departmentId, String nickName)
    {
        return userMapper.selectCount(organizationId, departmentId, nickName);
    }

	@OperationLog(title = "删除账号", content = "'账号id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception
    {
        return userMapper.updateStatus(id);
    }

	@OperationLog(title = "修改密码", content = "'账号id：' + #id", operationType = "2")
	@Override
    public int updatePassword(int id, String password) throws Exception
    {
    	User user = userMapper.selectById(id);
        user.setPassword(MD5Util.MD5(password));

        return userMapper.updatePassword(user.getPassword(), user.getUserId());
    }

	@OperationLog(title = "添加账号", content = "'账号名称：' + #entity.getUserName()", operationType = "0")
	@Override
    public int insert(User entity) throws Exception
    {
    	int count = userMapper.selectCountByName(entity.getUserName(), 0);
		if (count > 0) {
			// 账号已存在
			return -2;
		}
    	entity.setPassword(MD5Util.MD5(entity.getUserName()));

        return userMapper.insert(entity);
    }

	@OperationLog(title = "修改账号", content = "'账号名称：' + #entity.getUserName()", operationType = "2")
	@Override
    public int update(User entity) throws Exception
    {
    	int count = userMapper.selectCountByName(entity.getUserName(), entity.getUserId());
		if (count > 0) {
			// 账号已存在
			return -2;
		}
        return userMapper.updateById(entity);
    }

	@Override
    public List<Integer> getRoles(int userId)
    {
        return userMapper.selectUserRoles(userId);
    }

	@OperationLog(title = "设置账号角色", content = "'账号id：' + #userId + ', 角色id：' + #roleIds", operationType = "7")
	@Override
    public int setRole(int userId, String roleIds) throws Exception
    {
    	int result = userMapper.deleteUserRole(userId);
		if (roleIds != null && roleIds.length() > 0){
			List<Integer> roleIdArray = Arrays.asList(roleIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		    result = userMapper.insertUserRole(userId, roleIdArray);
		}
		return result;
    }
}
