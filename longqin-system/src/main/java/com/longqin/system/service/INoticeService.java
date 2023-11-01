package com.longqin.system.service;

import com.longqin.system.entity.Notice;

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
public interface INoticeService extends IService<Notice> {

	Notice getById(int id);
	
	List<Notice> getPage(int organizationId, String title, String beginDate, String endDate, int startIndex, int pageSize);
	
	int getCount(int organizationId, String title, String beginDate, String endDate);
	
	int delete(int id) throws Exception;
	
	int insert(Notice entity) throws Exception;
	
	int update(Notice entity) throws Exception;
	
	List<String> getNoticeFiles(int noticeId);
	
	int setNoticeFiles(int noticeId, String filePaths) throws Exception;
}
