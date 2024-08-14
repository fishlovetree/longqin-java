package com.longqin.business.service.impl;

import com.longqin.business.entity.WfFlow;
import com.longqin.business.entity.WfLink;
import com.longqin.business.entity.WfNode;
import com.longqin.business.mapper.WfFlowMapper;
import com.longqin.business.mapper.WfLinkMapper;
import com.longqin.business.mapper.WfNodeMapper;
import com.longqin.business.service.IWfFlowService;
import com.longqin.business.util.OperationLog;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class WfFlowServiceImpl extends ServiceImpl<WfFlowMapper, WfFlow> implements IWfFlowService {

	@Autowired
	WfFlowMapper wfFlowMapper;
	
	@Autowired
	WfNodeMapper wfNodeMapper;
	
	@Autowired
	WfLinkMapper wfLinkMapper;
	
	@Override
    public List<WfFlow> getFlowPage(int organizationId, String flowName, int startIndex, int pageSize){
        return wfFlowMapper.selectPage(organizationId, flowName, startIndex, pageSize);
    }
    
	@Override
    public int getFlowCount(int organizationId, String flowName){
        return wfFlowMapper.selectCount(organizationId, flowName);
    }
	
	@Override
	public WfFlow getFlowById(int flowId){
		return wfFlowMapper.selectById(flowId);
	}
	
	@Override
	public String getFlowJson(int flowId){
        List<WfNode> nodes = wfNodeMapper.selectFlowNodes(flowId);
        List<WfLink> links = wfLinkMapper.selectFlowLinks(flowId);
        StringBuilder sb = new StringBuilder("({rects:{");
        if (nodes != null && nodes.size() != 0)
        {
            int i = 0;
            for (WfNode node : nodes)
            {
                sb.append("rect" + i + ":{data:{\"id\":\"" + node.getNodeId() + "\",\"name\":\"" + node.getNodeName() 
                    + "\",\"rectType\":\"" + node.getNodeType() + "\",\"formId\":\"" + node.getFormId() + "\",\"cooperation\":\"" 
                	+ node.getCooperation() + "\",\"virtual\":\"" + node.getVirtual() + "\",\"departmentId\":\"" 
                    + (node.getDepartmentId() == null ? "" : node.getDepartmentId()) + "\",\"positionId\":\"" 
                	+ (node.getPositionId() == null ? "" : node.getPositionId()) + "\",\"userId\":\"" 
                	+ (node.getUserId() == null ? "" : node.getUserId()) + "\",\"remark\":\"" + node.getDescription() + "\"}, attr:{x:" + node.getPositionX() 
                	+ ",y:" + node.getPositionY() + "}, text:{\"text\":\"" + node.getNodeName() + "\"}}");
                sb.append(",");
                i++;
            }
            sb.deleteCharAt(sb.length() - 1);;
        }
        sb.append("},paths:{");
        if (links != null && links.size() != 0)
        {
            int j = 0;
            for (WfLink link : links)
            {
                sb.append("path" + j + ":{from:" + link.getFromNodeId() + ",to:" + link.getToNodeId() + ",data:{\"id\":\"" 
                    + link.getLinkId() + "\",\"name\":\"" + link.getLinkName() + "\",\"formId\":\"" + link.getFormId() 
                    + "\",\"field\":\"" + link.getField() + "\",\"operator\":\"" + link.getOperator() + "\",\"operatorValue\":\"" 
                    + link.getOperatorValue() + "\",\"remark\":\"" + link.getDescription() + "\"}, text:{\"text\":\"" 
                    + link.getLinkName() + "\"}, textPos:{x:" + link.getPositionX() + ",y:" + link.getPositionY() + "}}");
                sb.append(",");
                j++;
            }
            sb.deleteCharAt(sb.length() - 1);;
        }
        sb.append("}})");
        return sb.toString();
    }
	
	@OperationLog(title = "保存流程", content = "'流程名称：' + #entity.getFlowName()", operationType = "0")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int saveFlow(WfFlow entity){
		int result = -1;
        if (entity.getFlowId() == null)
        {
        	result = wfFlowMapper.insert(entity);
        }
        else
        {
            result = wfFlowMapper.updateById(entity);
        }
        if (result > 0)
        {
            wfNodeMapper.deleteByFlow(entity.getFlowId());
            wfLinkMapper.deleteByFlow(entity.getFlowId());
            Map<String, Integer> nodeMap = new HashMap<String, Integer>();
            String[] nodeArr = entity.getNodes().split(";");
            for (int i = 0; i < nodeArr.length; i++)
            {
                String[] node = nodeArr[i].split(",", -1);
                WfNode wfNode = new WfNode();
                wfNode.setGid(node[0]);
                wfNode.setPositionX(StringUtils.isEmpty(node[1]) ? null : Integer.parseInt(node[1]));
                wfNode.setPositionY(StringUtils.isEmpty(node[2]) ? null : Integer.parseInt(node[2]));
                wfNode.setNodeId(StringUtils.isEmpty(node[3]) ? null : Integer.parseInt(node[3]));
                wfNode.setNodeName(node[4]);
                wfNode.setNodeType(StringUtils.isEmpty(node[5]) ? null : Integer.parseInt(node[5]));
                wfNode.setFormId(StringUtils.isEmpty(node[6]) ? null : Integer.parseInt(node[6]));
                wfNode.setVirtual(StringUtils.isEmpty(node[7]) ? null : Integer.parseInt(node[7]));
                wfNode.setCooperation(StringUtils.isEmpty(node[8]) ? null : Integer.parseInt(node[8]));
                wfNode.setDepartmentId(StringUtils.isEmpty(node[9]) ? null : Integer.parseInt(node[9]));
                wfNode.setPositionId(StringUtils.isEmpty(node[10]) ? null : Integer.parseInt(node[10]));
                wfNode.setUserId(StringUtils.isEmpty(node[11]) ? null : Integer.parseInt(node[11]));
                wfNode.setDescription(node[12]);
                wfNode.setGroupSeq(StringUtils.isEmpty(node[13]) ? null : Integer.parseInt(node[13]));
                wfNode.setIsApproval(StringUtils.isEmpty(node[14]) ? null : Integer.parseInt(node[14]));
                wfNode.setFlowId(entity.getFlowId());
                wfNode.setCreator(entity.getCreator());
                wfNode.setOrganizationId(entity.getOrganizationId());
                wfNode.setStatus(1);
                if (wfNode.getNodeId() == null)
                {
                	wfNodeMapper.insert(wfNode);
                }
                else
                {
                	wfNodeMapper.updateById(wfNode);
                }
                nodeMap.put(wfNode.getGid(), wfNode.getNodeId());
            }
            String[] linkArr = entity.getLinks().split(";");
            for (int j = 0; j < linkArr.length; j++)
            {
                String[] link = linkArr[j].split(",", -1);
                WfLink wfLink = new WfLink();
                wfLink.setFromNodeId(nodeMap.get(link[0]));
                wfLink.setToNodeId(nodeMap.get(link[1]));
                wfLink.setLinkName(link[2]);
                wfLink.setPositionX(StringUtils.isEmpty(link[3]) ? null : Integer.parseInt(link[3]));
                wfLink.setPositionY(StringUtils.isEmpty(link[4]) ? null : Integer.parseInt(link[4]));
                wfLink.setFormId(StringUtils.isEmpty(link[5]) ? null : Integer.parseInt(link[5]));
                wfLink.setField(link[6]);
                wfLink.setOperator(link[7]);
                wfLink.setOperatorValue(link[8]);
                wfLink.setDescription(link[9]);
                wfLink.setCreator(entity.getCreator());
                wfLink.setFlowId(entity.getFlowId());
                wfLink.setOrganizationId(entity.getOrganizationId());

                wfLinkMapper.insert(wfLink);
            }
        }
        return result;
    }

	@OperationLog(title = "删除流程", content = "'流程id：' + #flowId", operationType = "1")
	@Override
    public int deleteFlow(int flowId){
        return wfFlowMapper.deleteFlow(flowId);
    }
}
