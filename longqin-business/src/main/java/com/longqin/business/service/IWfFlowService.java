package com.longqin.business.service;

import com.longqin.business.entity.WfFlow;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-11-01
 */
public interface IWfFlowService extends IService<WfFlow> {
	
	List<WfFlow> getFlowPage(int organizationId, int startIndex, int pageSize);

	int getFlowCount(int organizationId);
	
	WfFlow getFlowById(int flowId);
	
	String getFlowJson(int flowId);
	
	int saveFlow(WfFlow entity);
	
	int deleteFlow(int flowId);
}
