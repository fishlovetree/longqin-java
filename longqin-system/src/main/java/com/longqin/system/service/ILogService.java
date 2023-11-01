package com.longqin.system.service;

import com.longqin.system.entity.Log;

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
public interface ILogService extends IService<Log> {

	Log getById(int id);
	
	List<Log> getPage(int organizationId, String beginDate, String endDate, int startIndex, int pageSize);
	
	int getCount(int organizationId, String beginDate, String endDate);
	
	int insert(Log entity) throws Exception;
}
