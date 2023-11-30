package com.longqin.business.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.multipart.MultipartFile;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.WfProcess;
import com.longqin.business.entity.WfStep;
import com.longqin.business.service.IWfWorkService;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.longqin.business.util.SessionUtil;
import com.longqin.business.util.UploadUtils;

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
@RequestMapping("/wfWork")
@Api(tags="流程实例类")
public class WfWorkController {
	
	@Autowired
	IWfWorkService workService;

	/**
	 * @Description 获取流程开始节点表单
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取流程开始节点表单", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "flowId", value = "流程ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getFlowBeginNodeForm")
	public ResponseData getFlowBeginNodeForm(int flowId) {
		DesForm form = workService.getFlowBeginNodeForm(flowId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", form);
	}
	
	/**
	 * @Description 流程处理
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "流程处理", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "params", value = "参数集合", dataType = "Map<String,Object>") })
	@ApiResponses({ @ApiResponse(code = 1, message = "处理成功"), @ApiResponse(code = 0, message = "处理失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/dealWork")
	public ResponseData dealWork(@RequestBody Map<String,Object> params) throws Exception {
		if (null == params || params.size() == 0) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int flowId = (Integer)params.get("flowId");
		int processId = (Integer)params.get("processId");
		String tableName = (String)params.get("tableName");
		int isSave = (Integer)params.get("isSave");
		int direction = 1;
		if (params.get("approval_status") != null) {
			// 0表示不同意回退, 1表示同意前进
			direction = (Integer) params.get("approval_status");
		}
		String[] flowKeys = {"flowId", "processId", "tableName", "isSave"};
		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		params.forEach((key, value)->{
            if (!Arrays.asList(flowKeys).contains(key)){
            	columns.add(key);
            	values.add(value.toString());
            }
        });
		int result = workService.dealWork(flowId, processId, direction, tableName, columns, values, SessionUtil.getSessionUser().getUserId(), 
				SessionUtil.getSessionUser().getOrganizationId(), isSave == 1 ? true : false);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "处理成功", result);
		}
		else if (result == -2){
			return new ResponseData(ResponseEnum.ERROR.getCode(), "找不到下个节点处理人", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "创建失败", result);
		}
	}

	/**
	 * @Description 分页获取工作进程列表
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "分页获取工作列表", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "beginDate", value = "检索条件-开始日期", dataType = "String"),
		@ApiImplicitParam(name = "endDate", value = "检索条件-结束日期", dataType = "String"),
		@ApiImplicitParam(name = "status", value = "1-待办，0-已办", required = true, dataType = "int"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getProcessPage")
	public ResponseData getProcessPage(String beginDate, String endDate, Integer status, Integer page, Integer size) {
		if (null == status || null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<WfProcess> processList = workService.getProcessPage(SessionUtil.getSessionUser().getUserId(), status, beginDate, endDate, (page - 1) * size, size);
		int total = workService.getProcessCount(SessionUtil.getSessionUser().getUserId(), status, beginDate, endDate);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", processList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 获取流程进程表单
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取流程进程表单", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "processId", value = "流程进程ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getFlowProcessForm")
	public ResponseData getFlowProcessForm(int processId) {
		DesForm form = workService.getFlowProcessForm(processId);
        if (form == null) return new ResponseData(ResponseEnum.ERROR.getCode(), "查询失败");
        Map<String, String> formData = workService.getFlowProcessFormData(processId, form.getTableName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("form", form);
        map.put("formData", formData);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 获取流程进程表单数据
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取流程进程表单数据", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "processId", value = "流程进程ID", required = true, dataType = "int"),
		@ApiImplicitParam(name = "tableName", value = "表名称", required = true, dataType = "String")})
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getFlowProcessFormData")
	public ResponseData getFlowProcessFormData(int processId, String tableName) {
        Map<String, String> formData = workService.getFlowProcessFormData(processId, tableName);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", formData);
	}
	
	/**
	 * @Description 获取工作进程所有表单
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "获取工作进程所有表单", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "workId", value = "流程实例ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getWorkFlowProcessList")
	public ResponseData getWorkFlowProcessList(int workId) {
        List<Map<String, Object>> list = workService.getWorkProcessFormList(workId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", list);
	}
	
	/**
	 * @Description 工作转办
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "工作转办", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "processId", value = "流程进程ID", required = true, dataType = "int"),
		@ApiImplicitParam(name = "transferUser", value = "转办人ID逗号拼接", required = true, dataType = "String")})
	@ApiResponses({ @ApiResponse(code = 1, message = "转办成功"), @ApiResponse(code = 0, message = "转办失败"),
			@ApiResponse(code = 2, message = "表单已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/workTransfer")
	public ResponseData workTransfer(int processId, String transferUser) throws Exception {
		if (StringUtils.isEmpty(transferUser)) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = workService.workTransfer(processId, transferUser, SessionUtil.getSessionUser().getUserId(), SessionUtil.getSessionUser().getOrganizationId());
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "转办成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "转办失败", result);
		}
	}

	/**
	 * @Description 分页获取工作历史步骤列表
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "分页获取流程实例历史步骤列表", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "workId", value = "流程实例ID", required = true, dataType = "int"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getWorkSteps")
	public ResponseData getWorkSteps(Integer workId, Integer page, Integer size) {
		if (null == workId || null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<WfStep> stepList = workService.getStepPage(workId, (page - 1) * size, size);
		int total = workService.getStepCount(workId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", stepList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 文件上传
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "文件上传", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "files", value = "附件", required = true, dataType = "File") })
	@ApiResponses({ @ApiResponse(code = 1, message = "上传成功"), @ApiResponse(code = 0, message = "上传失败"),
		@ApiResponse(code = 3, message = "参数错误")  })
	@PostMapping("/uploadFiles")
	public ResponseData uploadFiles(MultipartFile[] files) throws Exception {
		if (null == files || files.length == 0) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<String> filePaths = new ArrayList<String>();
		for(int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String fileName = file.getOriginalFilename();
			String filePath = UploadUtils.coverUpload(file.getInputStream(), fileName);
			if(!filePath.equals("")) {
				filePaths.add(filePath);
			}
		}
		if (filePaths.size() > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "上传成功", filePaths);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "上传失败", filePaths);
		}
	}
	
	/**
	 * @Description 作废流程实例
	 * @Author longqin
	 * @Time: 2023年11月01日
	 */
	@ApiOperation(value = "作废流程实例", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "workId", value = "流程实例ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "作废成功"), @ApiResponse(code = 0, message = "作废失败") })
	@PostMapping("/disableWork")
	public ResponseData disableWork(int workId) throws Exception {
		int result = workService.disableWork(workId);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "作废成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "作废失败", result);
		}
	}
}
