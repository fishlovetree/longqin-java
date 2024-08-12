package com.longqin.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Department;
import com.longqin.system.service.IDepartmentService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  部门控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/department")
@Api(tags="部门类")
public class DepartmentController {

	@Autowired
	IDepartmentService departmentService;
	
	/**
	 * @Description 获取部门列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取部门列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getDepartmentList")
//	@RequiredPermission("department:view")
	public ResponseData getDepartmentList() {
		List<Department> departmentList = departmentService.getDepartmentList(SessionUtil.getSessionUser().getOrganizationId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", departmentList);
	}
	
	/**
	 * @Description 获取部门树形结构
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取部门树形结构", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getDepartmentTree")
	public ResponseData getDepartmentTree() {
		List<Map<String, Object>> departmentTree = departmentService.getDepartmentTree(SessionUtil.getSessionUser().getOrganizationId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", departmentTree);
	}
	
	/**
	 * @Description 添加部门
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加部门", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "department", value = "部门实体", required = true, dataType = "Department")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("department:view")
	public ResponseData create(@RequestBody Department department) throws Exception {
		if (null == department) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		department.setCreator(SessionUtil.getSessionUser().getUserId());
		if (0 == department.getOrganizationId()) {
			department.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int result = departmentService.addDepartment(department);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}
	
	/**
	 * @Description 修改部门
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改部门", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "department", value = "部门实体", required = true, dataType = "Department")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("department:view")
	public ResponseData update(@RequestBody Department department) throws Exception {
		if (null == department) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = departmentService.editDepartment(department);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}
	
	/**
	 * @Description 删除部门
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除部门", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "departmentId", value = "部门ID", required = true, dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("department:view")
	public ResponseData delete(int departmentId) throws Exception {
		int result = departmentService.deleteDepartment(departmentId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else if (result == -1) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "存在子部门，请先删除子部门", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
}
