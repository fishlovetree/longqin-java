package com.longqin.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.business.config.RequiredPermission;
import com.longqin.business.entity.WfFlow;
import com.longqin.business.service.IWfFlowService;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.longqin.business.util.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author longqin
 * @since 2023-11-01
 */
@RestController
@RequestMapping("/wfFlow")
@Api(tags="流程管理类")
public class WfFlowController {
	
	@Autowired
	IWfFlowService wfFlowService;

	/**
	 * @Description 获取单个流程
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取单个流程", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "flowId", value = "流程ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getById")
	public ResponseData getById(int flowId) {
		WfFlow flow = wfFlowService.getFlowById(flowId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", flow);
	}

	/**
	 * @Description 分页获取流程列表
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "分页获取流程", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getFlowPage")
	@RequiredPermission("flowList:view")
	public ResponseData getFlowPage(Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<WfFlow> flowList = wfFlowService.getFlowPage(SessionUtil.getSessionUser().getOrganizationId(), (page - 1) * size, size);
		int total = wfFlowService.getFlowCount(SessionUtil.getSessionUser().getOrganizationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", flowList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}

	/**
	 * @Description 保存流程
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "保存流程", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "entity", value = "流程实体项", required = true, dataType = "WfFlow") })
	@ApiResponses({ @ApiResponse(code = 1, message = "保存成功"), @ApiResponse(code = 0, message = "保存失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/save")
	@RequiredPermission("flowDesigner:view")
	public ResponseData save(@RequestBody WfFlow entity) throws Exception {
		if (null == entity || null == entity.getFlowName() || StringUtils.isEmpty(entity.getNodes()) || StringUtils.isEmpty(entity.getLinks())) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		entity.setCreator(SessionUtil.getSessionUser().getUserId());
		entity.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = wfFlowService.saveFlow(entity);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "保存成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "保存失败", result);
		}
	}
	
	/**
	 * @Description 获取流程节点连线JSON字符串
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取流程节点连线JSON字符串", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "flowId", value = "流程ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getFlowJson")
	public ResponseData getFlowJson(int flowId) {
		String json = wfFlowService.getFlowJson(flowId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", json);
	}

	/**
	 * @Description 删除流程
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "删除流程", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "flowId", value = "流程ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("flowList:view")
	public ResponseData delete(int flowId) throws Exception {
		int result = wfFlowService.deleteFlow(flowId);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
}
