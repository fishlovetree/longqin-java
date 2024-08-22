package com.longqin.business.service;

import com.longqin.business.entity.DiyTable;
import com.longqin.business.entity.DiyTableColumns;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-11-09
 */
public interface IDiyTableService extends IService<DiyTable> {

	DiyTable getById(int id);
	
	List<DiyTable> getPage(int organizationId, int startIndex, int pageSize);
	
	int getCount(int organizationId);
	
    int delete(int id) throws Exception;
    
    int create(DiyTable entity) throws Exception;
    
    int update(DiyTable entity) throws Exception;
    
    List<DiyTableColumns> getTableColumns(int id);
    
    int insertTableColumns(int tableId, List<DiyTableColumns> list) throws Exception;
    
    Map<String, Object> getTableData(Integer startIndex, Integer pageSize, Integer id, String dataSource, String selectedColumns, 
			int organizationId, Map<String, String> searchMap);
}
