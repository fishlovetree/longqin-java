package com.longqin.system.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.entity.Matter;
import com.longqin.system.service.IMatterService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 * 事项 前端控制器
 * </p>
 *
 * @author longqin
 * @since 2024-09-04
 */
@RestController
@RequestMapping("/matter")
public class MatterController {

	@Autowired
	IMatterService matterService;

	/**
	 * @Description 根据id获取事项
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "根据id获取事项", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "Integer id") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getById")
	public ResponseData getById(Integer id) throws Exception {
		if (null == id) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		Matter matter = matterService.getById(id);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", matter);
	}

	/**
	 * @Description 根据日期获取事项列表
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "根据日期获取事项列表", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "date", value = "日期", dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"),
		@ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getMatterList")
	public ResponseData getMatterList(String date) {
		if (StringUtils.isEmpty(date)) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<Matter> matterList = matterService.getList(date, SessionUtil.getSessionUser().getUserId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", matterList);
	}

	/**
	 * @Description 添加事项
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "添加事项", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "matter", value = "事项实体项", required = true, dataType = "Matter") })
	@ApiResponses({ @ApiResponse(code = 1, message = "添加成功"), @ApiResponse(code = 0, message = "添加失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	public ResponseData create(@RequestBody Matter matter) throws Exception {
		if (null == matter) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		matter.setCreator(SessionUtil.getSessionUser().getUserId());
		matter.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = matterService.insert(matter);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}

	/**
	 * @Description 修改事项
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "修改事项", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "matter", value = "事项实体项", required = true, dataType = "Matter") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	public ResponseData update(@RequestBody Matter matter) throws Exception {
		if (null == matter) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = matterService.update(matter);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}
	
	/**
	 * @Description 修改事项状态
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "修改事项状态", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败") })
	@PostMapping("/updateStatus")
	public ResponseData updateStatus(int id) throws Exception {
		int result = matterService.updateStatus(id);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除事项
	 * @Author longqin
	 * @Time: 2024年09月05日
	 */
	@ApiOperation(value = "删除事项", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "事项ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	public ResponseData delete(int id) throws Exception {
		int result = matterService.delete(id);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
}
