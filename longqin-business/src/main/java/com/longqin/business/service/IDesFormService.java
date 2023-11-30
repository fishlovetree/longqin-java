package com.longqin.business.service;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.entity.DesFormData;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-10-31
 */
public interface IDesFormService extends IService<DesForm> {

	int create(DesForm entity) throws Exception;
	
	int update(DesForm entity) throws Exception;
	
	int delete(int id) throws Exception;
	
	DesForm getById(int id);
	
	List<DesForm> getPage(int organizationId, int startIndex, int pageSize);
	
	int getCount(int organizationId);
	
	List<DesFormColumn> getTableColumns(String tableName);
	
	int insertFormData(DesFormData entity);
	
	int updateFormData(String tableName, List<String> columns, List<String> vals, Integer id);
}
