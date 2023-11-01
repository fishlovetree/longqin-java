package com.longqin.system.service;

import com.longqin.system.entity.Errorlog;

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
public interface IErrorlogService extends IService<Errorlog> {

    Errorlog getById(int id);
	
	List<Errorlog> getPage(String beginDate, String endDate, int startIndex, int pageSize);
	
	int getCount(String beginDate, String endDate);
	
	int insert(Errorlog entity);
}
