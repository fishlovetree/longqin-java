package com.longqin.system.service.impl;

import com.longqin.system.entity.Department;
import com.longqin.system.entity.Position;
import com.longqin.system.entity.User;
import com.longqin.system.mapper.DepartmentMapper;
import com.longqin.system.mapper.PositionMapper;
import com.longqin.system.mapper.UserMapper;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.MD5Util;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
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
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	PositionMapper positionMapper;

	@Override
    public User getById(int id) {
        return userMapper.selectById(id);
    }

	@Override
    public User getByName(String name) {
        return userMapper.selectByName(name);
    }

	@Override
    public int getCountByName(String name, int id) {
        return userMapper.selectCountByName(name, id);
    }

	@Override
    public List<User> getPage(int organizationId, String departmentId, String nickName, int startIndex, int pageSize) {
        return userMapper.selectPage(organizationId, departmentId, nickName, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId, String departmentId, String nickName) {
        return userMapper.selectCount(organizationId, departmentId, nickName);
    }

	@OperationLog(title = "删除账号", content = "'账号id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
        return userMapper.updateStatus(id);
    }

	@OperationLog(title = "修改密码", content = "'账号id：' + #id", operationType = "2")
	@Override
    public int updatePassword(int id, String password) throws Exception {
    	User user = userMapper.selectById(id);
        user.setPassword(MD5Util.MD5(password));

        return userMapper.updatePassword(user.getPassword(), user.getUserId());
    }

	@OperationLog(title = "添加账号", content = "'账号名称：' + #entity.getUserName()", operationType = "0")
	@Override
    public int insert(User entity) throws Exception {
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
    public int update(User entity) throws Exception {
    	int count = userMapper.selectCountByName(entity.getUserName(), entity.getUserId());
		if (count > 0) {
			// 账号已存在
			return -2;
		}
        return userMapper.updateById(entity);
    }

	@Override
    public List<Integer> getRoles(int userId) {
        return userMapper.selectUserRoles(userId);
    }

	@OperationLog(title = "设置账号角色", content = "'账号id：' + #userId + ', 角色id：' + #roleIds", operationType = "7")
	@Override
    public int setRole(int userId, String roleIds) throws Exception {
    	int result = userMapper.deleteUserRole(userId);
		if (roleIds != null && roleIds.length() > 0) {
			List<Integer> roleIdArray = Arrays.asList(roleIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		    result = userMapper.insertUserRole(userId, roleIdArray);
		}
		return result;
    }
	
	@Override
    public int getUserPositionLevel(int userId) {
        return userMapper.selectUserPositionLevel(userId);
    }

	// 获取流程处理人
	@Override
    public List<Integer> getFlowHandlers(Integer userId, Integer departmentId, Integer positionId, int submitterId) {
        List<Integer> handlers = new ArrayList<Integer>();
        User submitter = userMapper.selectById(submitterId);
        // 先判断节点是否配置指定用户
        if (userId != null && userId != 0) {
            handlers.add(userId);
        }
        else if (positionId != null && positionId != 0) {
            if (departmentId != null && departmentId != 0) {
                // 指定部门和职位
                handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, positionId);
            }
            else {
                // 只指定了职位,根据用户所属部门向上逐级查找
            	handlers = getUserByPosition(positionId, submitter.getDepartmentId());
            }
        }
        else if (departmentId != null && departmentId != 0) {
            // 只指定了部门,根据用户职位向上逐级查找
        	handlers = getUserByDepartment(submitter.getPositionId(), departmentId);
        }
        else {
            // 啥都没指定
        	handlers = getUserRecursion(submitter.getPositionId(), submitter.getDepartmentId());
        }
        return handlers;
    }

    // 指定职位，根据用户所属部门向上逐级查找
    private List<Integer> getUserByPosition(int positionId, int departmentId) {
    	List<Integer> handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, positionId);
        if (handlers == null || handlers.size() == 0) {
            Department dept = departmentMapper.selectById(departmentId);
            int parentId = dept.getParentId();
            if (parentId != -1) {
                // 递归获取处理人
            	handlers = getUserByPosition(positionId, parentId);
            }
        }
        return handlers;
    }

    // 指定部门，根据用户所属职位向上逐级查找
    private List<Integer> getUserByDepartment(int positionId, int departmentId) {
    	List<Integer> handlers = new ArrayList<>();
        Position position = positionMapper.selectById(positionId);
        int parentId = position.getParentId();
        if (parentId != -1) {
        	handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, parentId);
            if (handlers == null || handlers.size() == 0) {
                // 递归获取处理人
            	handlers = getUserByDepartment(parentId, departmentId);
            }
        }
        return handlers;
    }

    // 未指定部门和职位，根据用户所属职位和部门向上逐级查找
    private List<Integer> getUserRecursion(int positionId, int departmentId) {
    	List<Integer> handlers = new ArrayList<>();
        Position position = positionMapper.selectById(positionId);
        if (position != null && position.getParentId() != -1) {
            int parentId = position.getParentId();
            handlers = userMapper.selectUserIdByDeptAndPosition(departmentId, parentId);
            if (handlers == null || handlers.size() == 0) {
                // 递归部门获取处理人
            	handlers = getUserByPosition(parentId, departmentId);
                if (handlers == null || handlers.size() == 0) {
                    // 递归职位和部门获取处理人
                	handlers = getUserRecursion(parentId, departmentId);
                }
            }
        }
        return handlers;
    }
}
