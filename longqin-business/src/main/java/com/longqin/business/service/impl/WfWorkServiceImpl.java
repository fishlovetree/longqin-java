package com.longqin.business.service.impl;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.entity.DesFormData;
import com.longqin.business.entity.WfLink;
import com.longqin.business.entity.WfNode;
import com.longqin.business.entity.WfProcess;
import com.longqin.business.entity.WfStep;
import com.longqin.business.entity.WfWork;
import com.longqin.business.entity.WfWorkform;
import com.longqin.business.feign.FeignService;
import com.longqin.business.mapper.DesFormMapper;
import com.longqin.business.mapper.WfLinkMapper;
import com.longqin.business.mapper.WfNodeMapper;
import com.longqin.business.mapper.WfProcessMapper;
import com.longqin.business.mapper.WfStepMapper;
import com.longqin.business.mapper.WfWorkMapper;
import com.longqin.business.mapper.WfWorkformMapper;
import com.longqin.business.service.IWfWorkService;
import com.longqin.business.util.OperationLog;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-11-01
 */
@Service
public class WfWorkServiceImpl extends ServiceImpl<WfWorkMapper, WfWork> implements IWfWorkService {
	
	@Autowired
	WfNodeMapper nodeMapper;

	@Autowired
	WfWorkMapper workMapper;
	
	@Autowired
	WfProcessMapper processMapper;
	
	@Autowired
	DesFormMapper formMapper;
	
	@Autowired
	WfWorkformMapper workformMapper;
	
	@Autowired
	WfStepMapper stepMapper;
	
	@Autowired
	WfLinkMapper linkMapper;
	
	@Autowired
	FeignService feignService;
	
	@Override
    public DesForm getFlowBeginNodeForm(int flowId) {
        return nodeMapper.selectFlowBeginNodeForm(flowId);
    }

	 // 处理用户提交的工作
	@OperationLog(title = "处理用户提交的工作", content = "'工作id：' + #processId", operationType = "5")
	@Transactional(rollbackFor = Exception.class)
	@Override
    public int dealWork(int flowId, int processId, int action, String tableName,
        List<String> columns, List<String> values, int submitter, int organizationId, Boolean isSave) throws Exception{
        // 先处理表单数据，增加创建人、创建时间、组织机构、表单状态
        columns.add("creator");
        values.add(String.valueOf(submitter));
        columns.add("create_time");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.add(format.format(new Date()));
        columns.add("organization_id");
        values.add(String.valueOf(organizationId));
        columns.add("status");
        values.add("1");

        int result = -1;
        if (processId == 0) {
            // 工作还没创建
            // 先创建工作实例
            WfNode beginNode = nodeMapper.selectFlowBeginNode(flowId);
            int nodeId = beginNode.getNodeId();
            WfWork work = new WfWork();
            work.setFlowId(flowId);
            work.setCreator(submitter);
            work.setOrganizationId(organizationId);

            result = workMapper.insert(work);
            int workId = work.getWorkId();
            // 生成一条指向自己的待办
            WfProcess process = new WfProcess();
            process.setWorkId(workId);
            process.setNodeId(nodeId);
            process.setLinkId(0);
            process.setSendingTo(submitter);
            process.setProcessType(1);
            process.setSubmitter(submitter);
            process.setStatus(1);
            process.setOrganizationId(organizationId);
            processMapper.insert(process);
            processId = process.getProcessId();
            // 插入表单数据
            dealFormData(workId, processId, nodeId, tableName, columns, values, 0);
            if (!isSave) {
                // 流程流转
                result = excuteFlow(flowId, workId, processId, nodeId, action, columns, values, submitter, organizationId);
            }
            else {
                // 返回代办工作ID
                result = processId;
            }
        }
        else {
            WfProcess flowProcess = processMapper.selectProcessById(processId);
            // 插入表单数据
            dealFormData(flowProcess.getWorkId(), processId, flowProcess.getNodeId(), tableName, columns, values, flowProcess.getFormDataId() == null ? 0 : flowProcess.getFormDataId());
            if (!isSave) {
                // 流程流转
                result = excuteFlow(flowProcess.getFlowId(), flowProcess.getWorkId(), processId, flowProcess.getNodeId(), action, columns, values, submitter, organizationId);
            }
            else{
            	// 返回代办工作ID
                result = processId;
            }
        }
        if (result == -2){
        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    // 流程流转
    // 返回值：1-成功，-1失败，-2没找到处理人
    private int excuteFlow(int flowId, int workId, int processId, int nodeId, int action, List<String> columns, List<String> values, 
    		int submitter, int organizationId) {
        int sendingTo = 0;
        // 插入操作步骤
        WfStep step = new WfStep();
        step.setWorkId(workId);
        step.setNodeId(nodeId);
        step.setProcessId(processId);
        step.setSubmitter(submitter);
        step.setOrganizationId(organizationId);
        step.setAction(action);
        stepMapper.insert(step);
        if (action == 1) { // 前进
            // 获取当前节点
        	WfNode currentNode = nodeMapper.selectById(nodeId);
            Boolean needCooperation = needCooperation(workId, currentNode);
            if (!needCooperation) {
                // 无需多人协作，关闭当前节点所有待办
            	processMapper.closeNodeProcess(workId, nodeId);
                // 获取节点连线
                List<WfLink> links = linkMapper.selectNodeLinks(nodeId);
                if (links.size() == 0) {
                    // 没有后继节点，结束流程
                	workMapper.closeWork(workId);
                    return 1;
                }
                // 创建下个节点待办
                // 判断是否是普通节点或者合流点（只有普通节点或合流点存在条件走向，或后继节点可能是合流点）
                if (currentNode.getNodeType() == 0 || currentNode.getNodeType() == 2) {
                    // 判断是否有条件
                    int toNodeId = 0;
                    int linkId = 0;
                    for (WfLink link : links) {
                        String field = link.getField();
                        String operatorName = link.getOperator();
                        String operatorValue = link.getOperatorValue();
                        // 连线有条件
                        if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(operatorName) && !StringUtils.isEmpty(operatorValue)) {
                            // 判断条件表单，不为0表示取其他节点表单
                            if (link.getFormId() != 0) {
                            	DesForm form = formMapper.selectById(link.getFormId());
                            	List<DesFormColumn> formColumns = formMapper.selectTableColumns(form.getTableName());
                            	// 获取表单数据
                            	Map<String, String> valueMap = workformMapper.selectTableDatas(form.getTableName(), workId);
                            	columns = new ArrayList<String>();
                            	values = new ArrayList<String>();
                            	for (DesFormColumn formColumn : formColumns) {
                            		columns.add(formColumn.getColumnName());
                            		if (valueMap.containsKey(formColumn.getColumnName())) {
                            			values.add(valueMap.get(formColumn.getColumnName()));
                            		}
                            		else {
                            			values.add("");
                            		}
                            	}
                            }
                            // 有条件，判断满足哪个条件
                            if (field.equals("userId")) {
                            	String submitterStr = String.valueOf(submitter);
                                // 条件是提交人
                                switch (operatorName) {
                                    case "=":
                                        if (submitterStr.equals(operatorValue)) {
                                            toNodeId = link.getToNodeId();
                                            linkId = link.getLinkId();
                                        }
                                        break;
                                    case "!=":
                                        if (!submitterStr.equals(operatorValue)) {
                                        	toNodeId = link.getToNodeId();
                                            linkId = link.getLinkId();
                                        }
                                        break;
                                }
                            }
                            else if (field.equals("positionLevel")) {
                                // 条件是提交人职级
                                // 获取提交人最高职级
                            	ResponseData levelResponse = feignService.getUserPositionLevel(submitter);
                            	if (levelResponse.getCode() == ResponseEnum.SUCCESS.getCode()) {
                            		int positionLevel = (Integer)levelResponse.getData();
                            		int val = Integer.parseInt(operatorValue);
                            		switch (operatorName) {
                                        case "=":
                                            if (val == positionLevel) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                        case "!=":
                                            if (val != positionLevel) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                        case ">":
                                            if (positionLevel > val) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                        case "<":
                                            if (positionLevel < val) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                        case ">=":
                                            if (positionLevel >= val) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                        case "<=":
                                            if (positionLevel <= val) {
                                            	toNodeId = link.getToNodeId();
                                                linkId = link.getLinkId();
                                            }
                                            break;
                                    }
                            	}
                            }
                            else {
                                if (columns == null || values == null || columns.size() != values.size()) continue;
                                for (int i = 0; i < columns.size(); i++) {
                                    String column = columns.get(i);
                                    String value = values.get(i);
                                    if (field.equals(column)) {
                                        switch (operatorName) {
                                            case "=":
                                                if (value.compareTo(operatorValue) == 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                            case "!=":
                                                if (value.compareTo(operatorValue) != 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                            case ">":
                                                if (value.compareTo(operatorValue) > 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                            case "<":
                                                if (value.compareTo(operatorValue) < 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                            case ">=":
                                                if (value.compareTo(operatorValue) >= 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                            case "<=":
                                                if (value.compareTo(operatorValue) <= 0) {
                                                	toNodeId = link.getToNodeId();
                                                    linkId = link.getLinkId();
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (toNodeId == 0) {
                        // 没有条件，则默认取第一个线路
                        WfLink link = links.get(0);
                        WfNode toNode = nodeMapper.selectById(link.getToNodeId());
                        if (toNode.getNodeType() == 2 || toNode.getNodeType() == 3) {
                            // 后继节点是合流点或者分合流点，则需判断前置节点待办是否都已处理
                        	int preNodeProcess = processMapper.selectPreNodeProcessCount(workId, link.getToNodeId());
                            if (preNodeProcess == 0) {
                                // 前驱节点都已关闭，创建后继节点的待办，否则不做处理
                                ResponseData handlersResponse = feignService.getFlowHandlers(toNode.getUserId(), toNode.getDepartmentId(), toNode.getPositionId(), submitter);
                                if (handlersResponse.getCode() != ResponseEnum.SUCCESS.getCode()) {
                                    return -2;
                                }
                                List<Integer> handlers = (List<Integer>)handlersResponse.getData();
                                if (handlers == null || handlers.size() == 0) {
                                	return -2;
                                }
                                // 默认返回第一个处理人
                                sendingTo = handlers.get(0);
                                for (Integer handler : handlers) {
                                    // 创建代办工作
                                    WfProcess process = new WfProcess();
                                    process.setWorkId(workId);
                                    process.setNodeId(link.getToNodeId());
                                    process.setLinkId(link.getLinkId());
                                    process.setSendingTo(handler);
                                    process.setProcessType(1);
                                    process.setSubmitter(submitter);
                                    process.setOrganizationId(organizationId);
                                    process.setStatus(1);
                                    processMapper.insert(process);
                                }
                            }
                        }
                        else {
                            // 后继节点是普通或者分流点，则直接生成待办工作
                        	ResponseData handlersResponse = feignService.getFlowHandlers(toNode.getUserId(), toNode.getDepartmentId(), toNode.getPositionId(), submitter);
                            if (handlersResponse.getCode() != ResponseEnum.SUCCESS.getCode()) {
                                return -2;
                            }
                            List<Integer> handlers = (List<Integer>)handlersResponse.getData();
                            if (handlers == null || handlers.size() == 0) {
                            	return -2;
                            }
                            // 默认返回第一个处理人
                            sendingTo = handlers.get(0);
                            for (Integer handler : handlers) {
                                // 创建代办工作
                                WfProcess process = new WfProcess();
                                process.setWorkId(workId);
                                process.setNodeId(link.getToNodeId());
                                process.setLinkId(link.getLinkId());
                                process.setSendingTo(handler);
                                process.setProcessType(1);
                                process.setSubmitter(submitter);
                                process.setOrganizationId(organizationId);
                                process.setStatus(1);
                                processMapper.insert(process);
                            }
                        }
                    }
                    else {
                        // 有条件，则直接生成待办工作
                    	WfNode toNode = nodeMapper.selectById(toNodeId);
                        ResponseData handlersResponse = feignService.getFlowHandlers(toNode.getUserId(), toNode.getDepartmentId(), toNode.getPositionId(), submitter);
                        if (handlersResponse.getCode() != ResponseEnum.SUCCESS.getCode()) {
                            return -2;
                        }
                        List<Integer> handlers = (List<Integer>)handlersResponse.getData();
                        if (handlers == null || handlers.size() == 0) {
                        	return -2;
                        }
                        // 默认返回第一个处理人
                        sendingTo = handlers.get(0);
                        for (Integer handler : handlers) {
                            // 创建代办工作
                            WfProcess process = new WfProcess();
                            process.setWorkId(workId);
                            process.setNodeId(toNodeId);
                            process.setLinkId(linkId);
                            process.setSendingTo(handler);
                            process.setProcessType(1);
                            process.setSubmitter(submitter);
                            process.setOrganizationId(organizationId);
                            process.setStatus(1);
                            processMapper.insert(process);
                        }
                    }
                }
                else if (currentNode.getNodeType() == 1 || currentNode.getNodeType() == 3) {
                    // 分流节点或者分合流点
                    // 遍历后继节点分别创建待办工作
                    for (WfLink link : links) {
                    	WfNode toNode = nodeMapper.selectById(link.getToNodeId());
                        ResponseData handlersResponse = feignService.getFlowHandlers(toNode.getUserId(), toNode.getDepartmentId(), toNode.getPositionId(), submitter);
                        if (handlersResponse.getCode() != ResponseEnum.SUCCESS.getCode()) {
                            return -2;
                        }
                        List<Integer> handlers = (List<Integer>)handlersResponse.getData();
                        if (handlers == null || handlers.size() == 0) {
                        	return -2;
                        }
                        // 默认返回第一个处理人
                        sendingTo = handlers.get(0);
                        for (Integer handler : handlers) {
                            // 创建代办工作
                            WfProcess process = new WfProcess();
                            process.setWorkId(workId);
                            process.setNodeId(link.getToNodeId());
                            process.setLinkId(link.getLinkId());
                            process.setSendingTo(handler);
                            process.setProcessType(1);
                            process.setSubmitter(submitter);
                            process.setOrganizationId(organizationId);
                            process.setStatus(1);
                            processMapper.insert(process);
                        }
                    }
                }
            }
            else {
                // 需多人协作，只关闭当前处理人待办
            	processMapper.closeProcessById(processId);
            }
        }
        else {
            // 作废所有已办和未办工作
        	processMapper.disableProcess(workId);
            // 后退到开始节点
            WfNode beginNode = nodeMapper.selectFlowBeginNode(flowId);
            WfProcess beginProcess = processMapper.selectNodeFirstProcess(workId, beginNode.getNodeId());
            WfWorkform form = workformMapper.selectByProcessId(beginProcess.getProcessId());
            // 默认返回第一个处理人
            sendingTo = beginProcess.getSendingTo();
            beginProcess.setProcessId(null);
            beginProcess.setStatus(1);
            processMapper.insert(beginProcess);
            int newProcessId = beginProcess.getProcessId();
            // 删除原有关联
            if (form != null) {
                workformMapper.deleteById(form.getId());
            }
            form.setId(null);
            form.setProcessId(newProcessId);
            workformMapper.insert(form);
        }
        return sendingTo;
    }

    // 处理业务数据
    private int dealFormData(int workId, int processId, int nodeId, String tableName, List<String> columns, List<String> values, int formDataId) {
        if (formDataId == 0) {
        	DesFormData formData = new DesFormData();
        	formData.setTableName(tableName);
        	formData.setColumns(String.join(",", columns));
        	values = values.stream().map(s -> "'" + s + "'").collect(Collectors.toList());
        	formData.setVals(String.join(",", values));
            formMapper.insertFormData(formData);
            formDataId = formData.getId();
            WfWorkform workForm = new WfWorkform();
            workForm.setWorkId(workId);
            workForm.setProcessId(processId);
            workForm.setNodeId(nodeId);
            workForm.setTableName(tableName);
            workForm.setFormDataId(formDataId);
            return workformMapper.insert(workForm);
        }
        else {
        	StringBuilder sb = new StringBuilder("");
        	for(int i = 0; i < columns.size(); i++) {
    			String column = columns.get(i);
    			String val = values.get(i);
    			sb.append(column + "='" + val + "',");
    		}
        	sb.deleteCharAt(sb.length() - 1);
        	return formMapper.updateFormData(tableName, sb.toString(), formDataId);
        }
    }

    // 是否需要多人协作（false表示不需要或者是最后一个处理人）
    private Boolean needCooperation(int workId, WfNode node) {
        if (node.getCooperation() == 1) {
            int processCount = processMapper.selectNodeProcessCount(workId, node.getNodeId());
            if (processCount > 1)  {
                return true;
            }
            else {
                return false;
            }
        }
        else  {
            return false;
        }
    }
    
    @Override
    public List<WfProcess> getProcessPage(int userId, int status, String beginDate, String endDate, int startIndex, int pageSize) {
        return processMapper.selectPage(userId, status, beginDate, endDate, startIndex, pageSize);
    }
    
    @Override
    public int getProcessCount(int userId, int status, String beginDate, String endDate) {
        return processMapper.selectCount(userId, status, beginDate, endDate);
    }
    
    @Override
    public DesForm getFlowProcessForm(int processId) {
    	return processMapper.selectProcessForm(processId);
    }
    
    @Override
    public Map<String, String> getFlowProcessFormData(int processId, String tableName) {
    	return processMapper.selectProcessFormData(processId, tableName);
    }

    // 获取已处理的表单列表
    @Override
    public List<Map<String, Object>> getWorkProcessFormList(int workId)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<WfWorkform> list = workformMapper.selectWorkFormList(workId);
        if (list != null) {
            for(WfWorkform item : list) {
            	Map<String, String> formData = workformMapper.selectTableDatas(item.getTableName(), item.getProcessId());
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("form", item);
            	map.put("formData", formData);
                result.add(map);
            }
        }
        return result;
    }

    // 工作转办
    @OperationLog(title = "工作转办", content = "'工作id：' + #processId", operationType = "5")
	@Transactional(rollbackFor = Exception.class)
    @Override
    public int workTransfer(int processId, String transferUser, int submitter, int organizationId)
    {
    	int result = -1;
    	WfProcess flowProcess = processMapper.selectProcessById(processId);
        // 插入操作步骤
        WfStep step = new WfStep();
        step.setWorkId(flowProcess.getWorkId());
        step.setNodeId(flowProcess.getNodeId());
        step.setProcessId(processId);
        step.setSubmitter(submitter);
        step.setOrganizationId(organizationId);
        step.setAction(3); // 转办
        stepMapper.insert(step);
        processMapper.closeProcessById(processId);
        flowProcess.setProcessId(null);
        flowProcess.setStatus(1);
        String[] users = transferUser.split(",");
        for(int i = 0; i < users.length; i++) {
            flowProcess.setSendingTo(Integer.parseInt(users[i])); 
            result = processMapper.insert(flowProcess);
        }
        return result;
    }
    
    @Override
    public List<WfStep> getStepPage(int workId, int startIndex, int pageSize) {
    	List<WfStep> steps = stepMapper.selectPage(workId, startIndex, pageSize);
    	for (WfStep wfStep : steps) {
    		LocalDateTime toDateTime = wfStep.getSubmitTime();
    		LocalDateTime tempDateTime = wfStep.getBeginTime();
    		long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS);
    		tempDateTime = tempDateTime.plusDays( days );
    		long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS);
    		tempDateTime = tempDateTime.plusHours( hours );
    		long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES);
    		tempDateTime = tempDateTime.plusMinutes( minutes );
    		long seconds = tempDateTime.until( toDateTime, ChronoUnit.SECONDS);

    		wfStep.setStayTime(days + "天" + hours + "小时" + minutes + "分" + seconds + "秒");
		}
    	return steps;
    }
    
    @Override
    public int getStepCount(int workId) {
        return stepMapper.selectCount(workId);
    }

    @OperationLog(title = "作废流程实例", content = "'流程实例id：' + #workId", operationType = "1")
    @Override
    public int disableWork(int workId) throws Exception {
    	processMapper.disableProcess(workId);
        return workMapper.disableWork(workId);
    }
    
    @Override
    public int getCreator(int workId) {
        return workMapper.getCreator(workId);
    }
}
