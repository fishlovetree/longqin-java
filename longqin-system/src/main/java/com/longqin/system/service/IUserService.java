package com.longqin.system.service;

import com.longqin.system.entity.User;
import com.longqin.system.util.ResponseData;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
public interface IUserService extends IService<User> {
	
    User getById(int id);

    User getByName(String name);

    int getCountByName(String name, int id);

    List<User> getPage(int organizationId, String departmentId, String nickName, int startIndex, int pageSize);
    
    int getCount(int organizationId, String departmentId, String nickName);

    int delete(int id) throws Exception;

    int updatePassword(int id, String password) throws Exception;

    int insert(User model) throws Exception;

    int update(User model) throws Exception;

    List<Integer> getRoles(int userId);

    int setRole(int userId, String roleIds) throws Exception;
    
    void exportTemplate(Integer organizationId);
    
    ResponseData importUser(Integer organizationId, MultipartFile file);
    
    void export(Integer organizationId, String departmentId, String nickName);
    
    int getUserPositionLevel(int userId);
    
    List<Integer> getFlowHandlers(Integer userId, Integer departmentId, Integer positionId, int submitterId);
}
