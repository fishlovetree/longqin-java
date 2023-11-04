package com.longqin.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Role;
import com.longqin.system.service.IRoleService;
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
 *  角色控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/role")
@Api(tags="角色类")
public class RoleController {

	@Autowired
	IRoleService roleService;

	/**
	 * @Description 获取角色列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取角色列表", httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getRoleList")
	public ResponseData getRoleList() {
		List<Role> roleList = roleService.getList(SessionUtil.getSessionUser().getOrganizationId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", roleList);
	}

	/**
	 * @Description 添加角色
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加角色", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "角色实体项", required = true, dataType = "Role") })
	@ApiResponses({ @ApiResponse(code = 1, message = "添加成功"), @ApiResponse(code = 0, message = "添加失败"),
			 @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("role:view")
	public ResponseData create(Role role) throws Exception {
		if (null == role) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		role.setCreator(SessionUtil.getSessionUser().getUserId());
		if (null == role.getOrganizationId()) {
			role.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int result = roleService.insert(role);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}

	/**
	 * @Description 修改角色
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改角色", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "角色实体项", required = true, dataType = "Role") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	@RequiredPermission("role:view")
	public ResponseData update(Role role) throws Exception {
		if (null == role) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = roleService.update(role);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除角色
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除角色", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("role:view")
	public ResponseData delete(int roleId) throws Exception {
		int result = roleService.delete(roleId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}

	/**
	 * @Description 获取角色菜单列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取角色菜单列表", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getRoleMenu")
	@RequiredPermission("role:view")
	public ResponseData getRoleMenu(int roleId) {
		List<Integer> menuList = roleService.getMenus(roleId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuList);
	}

	/**
	 * @Description 设置角色菜单
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "设置角色菜单", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "int"),
			@ApiImplicitParam(name = "menuIdStr", value = "逗号拼接的菜单ID字符串", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "设置成功"), @ApiResponse(code = 0, message = "设置失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/updateRoleMenu")
	@RequiredPermission("role:view")
	public ResponseData updateRoleMenu(int roleId, String menuIdStr) throws Exception {
		if (null == menuIdStr) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		int result = roleService.setMenu(roleId, menuIdStr);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "设置成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "设置失败", result);
		}
	}
}
