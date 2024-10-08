package com.longqin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Organization;
import com.longqin.system.service.IOrganizationService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  公司类控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/organization")
@Api(tags="公司类")
public class OrganizationController {

	@Autowired
	IOrganizationService organizationService;
	
	/**
	 * @Description 根据id获取公司
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "根据id获取公司", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "orgId", value = "公司id", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getById")
	public ResponseData getById(int orgId) {
		Organization org = organizationService.getById(orgId);
		if (org == null){
			return new ResponseData(ResponseEnum.ERROR.getCode(), "查询失败");
		}
		else{
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", org);
		}
	}

	/**
	 * @Description 获取公司列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取公司列表", httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getOrganizationList")
//	@RequiredPermission("organization:view")
	public ResponseData getOrganizationList() {
		List<Organization> orgList = organizationService.getList();
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", orgList);
	}

	/**
	 * @Description 分页获取公司列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "分页获取公司", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getOrganizationPage")
	@RequiredPermission("organization:view")
	public ResponseData getOrganizationPage(Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<Organization> organizationList = organizationService.getPage((page - 1) * size, size);
		int total = organizationService.getCount();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", organizationList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}

	/**
	 * @Description 添加公司
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加公司", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "organization", value = "公司实体项", required = true, dataType = "Organization") })
	@ApiResponses({ @ApiResponse(code = 1, message = "添加成功"), @ApiResponse(code = 0, message = "添加失败"),
			@ApiResponse(code = 2, message = "公司已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("organization:view")
	public ResponseData create(@RequestBody Organization organization) throws Exception {
		if (null == organization) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = organizationService.insert(organization);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else if (result == -2) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "公司已存在", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}

	/**
	 * @Description 修改公司
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改公司", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "organization", value = "公司实体项", required = true, dataType = "Organization") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 2, message = "公司已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
//	@RequiredPermission("organization:view")
	public ResponseData update(@RequestBody Organization organization) throws Exception {
		if (null == organization) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = organizationService.update(organization);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else if (result == -2) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "公司已存在", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除公司
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除公司", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "organizationId", value = "公司ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("organization:view")
	public ResponseData delete(int organizationId) throws Exception {
		int result = organizationService.delete(organizationId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
}
