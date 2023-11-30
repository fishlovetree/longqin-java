package com.longqin.business.service;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.WfProcess;
import com.longqin.business.entity.WfStep;
import com.longqin.business.entity.WfWork;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author longqin
 * @since 2023-11-01
 */
public interface IWfWorkService extends IService<WfWork> {

	DesForm getFlowBeginNodeForm(int flowId);
	
	int dealWork(int flowId, int processId, int action, String tableName,
	        List<String> columns, List<String> values, int submitter, int organizationId, Boolean isSave) throws Exception;
	
	List<WfProcess> getProcessPage(int userId, int status, String beginDate, String endDate, int startIndex, int pageSize);
	
	int getProcessCount(int userId, int status, String beginDate, String endDate);
	
	DesForm getFlowProcessForm(int processId);
	
	Map<String, String> getFlowProcessFormData(int processId, String tableName);
	
	List<Map<String, Object>> getWorkProcessFormList(int workId);
	
	int workTransfer(int processId, String transferUser, int submitter, int organizationId);
	
	List<WfStep> getStepPage(int workId, int startIndex, int pageSize);
	
	int getStepCount(int workId);
	
	int disableWork(int workId) throws Exception;
}
