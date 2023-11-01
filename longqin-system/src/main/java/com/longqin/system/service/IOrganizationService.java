package com.longqin.system.service;

import com.longqin.system.entity.Organization;

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
public interface IOrganizationService extends IService<Organization> {

	Organization getById(int id);
	
	Organization getByCode(String code);
	
	Organization getByName(String name);
	
	List<Organization> getList();
	
	List<Organization> getPage(int startIndex, int pageSize);
	
	int getCount();
	
	int delete(int id) throws Exception;
	
	int insert(Organization entity) throws Exception;
	
	int update(Organization entity) throws Exception;
}
