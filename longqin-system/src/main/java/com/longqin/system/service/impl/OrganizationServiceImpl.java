package com.longqin.system.service.impl;

import com.longqin.system.entity.Organization;
import com.longqin.system.mapper.OrganizationMapper;
import com.longqin.system.service.IOrganizationService;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

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
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

	@Autowired
	OrganizationMapper organizationMapper;

	@Override
    public Organization getById(int id) {
        return organizationMapper.selectById(id);
    }
    
	@Override
    public Organization getByCode(String code) {
        return organizationMapper.selectByCode(code);
    }

	@Override
    public Organization getByName(String name) {
        return organizationMapper.selectByName(name);
    }
    
	@Override
    public List<Organization> getList() {
        return organizationMapper.selectList();
    }

	@Override
    public List<Organization> getPage(int startIndex, int pageSize) {
        return organizationMapper.selectPage(startIndex, pageSize);
    }
    
	@Override
    public int getCount() {
        return organizationMapper.selectCount();
    }

	@OperationLog(title = "删除公司", content = "'公司id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
        return organizationMapper.updateStatus(id);
    }
    
	@OperationLog(title = "添加公司", content = "'公司名称：' + #entity.getOrganizationName()", operationType = "0")
	@Override
    public int insert(Organization entity) throws Exception {
    	int count = organizationMapper.selectCountByName(entity.getOrganizationName(), 0);
		if (count > 0) {
			// 公司名已存在
			return -2;
		}
        return organizationMapper.insert(entity);
    }

	@OperationLog(title = "修改公司", content = "'公司名称：' + #entity.getOrganizationName()", operationType = "2")
	@Override
    public int update(Organization entity) throws Exception {
    	int count = organizationMapper.selectCountByName(entity.getOrganizationName(), entity.getOrganizationId());
		if (count > 0) {
			// 账号已存在
			return -2;
		}
        return organizationMapper.updateById(entity);
    }
}
