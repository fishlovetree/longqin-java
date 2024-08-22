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
import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.service.IDesFormService;
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
 * @since 2023-10-31
 */
@RestController
@RequestMapping("/desForm")
@Api(tags="自定义表单类")
public class DesFormController {
	
	@Autowired
	IDesFormService desFormService;
	
	/**
	 * @Description 获取单个表单
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "获取单个表单", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "表单ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getById")
	public ResponseData getById(int id) {
		DesForm form = desFormService.getById(id);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", form);
	}

	/**
	 * @Description 分页获取表单列表
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "分页获取表单", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getFormPage")
//	@RequiredPermission("formList:view")
	public ResponseData getFormPage(Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<DesForm> formList = desFormService.getPage(SessionUtil.getSessionUser().getOrganizationId(), (page - 1) * size, size);
		int total = desFormService.getCount(SessionUtil.getSessionUser().getOrganizationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", formList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}

	/**
	 * @Description 创建表单
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "创建表单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "entity", value = "表单实体项", required = true, dataType = "DesForm") })
	@ApiResponses({ @ApiResponse(code = 1, message = "创建成功"), @ApiResponse(code = 0, message = "创建失败"),
			@ApiResponse(code = 2, message = "表单已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("formDesigner:view")
	public ResponseData create(@RequestBody DesForm entity) throws Exception {
		if (null == entity || StringUtils.isEmpty(entity.getTableName()) || StringUtils.isEmpty(entity.getJsonData())) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		entity.setCreator(SessionUtil.getSessionUser().getUserId());
		entity.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = desFormService.create(entity);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "创建成功", result);
		}
		else if (result == -2){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "表单已存在", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "创建失败", result);
		}
	}

	/**
	 * @Description 修改表单
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "修改表单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "entity", value = "表单实体项", required = true, dataType = "DesForm") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 2, message = "表单已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	@RequiredPermission("formDesigner:view")
	public ResponseData update(@RequestBody DesForm entity) throws Exception {
		if (null == entity || null == entity.getId() || StringUtils.isEmpty(entity.getTableName()) || StringUtils.isEmpty(entity.getJsonData())) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		entity.setCreator(SessionUtil.getSessionUser().getUserId());
		entity.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = desFormService.update(entity);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else if (result == -2){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "表单已存在", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除表单
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "删除表单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "表单ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("formList:view")
	public ResponseData delete(int id) throws Exception {
		int result = desFormService.delete(id);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
	
	/**
	 * @Description 获取表单数据库列
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "获取表单数据库列", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tableName", value = "数据库表名", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getTableColumns")
	public ResponseData getTableColumns(String tableName) {
		List<DesFormColumn> columns = desFormService.getTableColumns(tableName);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", columns);
	}
	
	/**
	 * @Description 根据数据库表名获取表单信息
	 * @Author longqin
	 * @Time: 2023年10月31日
	 */
	@ApiOperation(value = "根据数据库表名获取表单信息", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tableName", value = "数据库表名", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getByTableName")
	public ResponseData getByTableName(String tableName) {
		DesForm form = desFormService.getByTableName(tableName);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", form);
	}
}
