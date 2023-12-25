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
import com.longqin.system.entity.Menu;
import com.longqin.system.service.IMenuService;
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
 *  菜单控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/menu")
@Api(tags="菜单类")
public class MenuController {

	@Autowired
	IMenuService menuService;
	
	/**
	 * @Description 获取菜单列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取菜单列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getMenuList")
	@RequiredPermission("menu:view")
	public ResponseData getMenuList() {
		List<Menu> menuList = menuService.getMenuList();
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuList);
	}
	
	/**
	 * @Description 获取菜单树形结构
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取菜单树形结构", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getMenuTree")
	@RequiredPermission("menu:view")
	public ResponseData getMenuTree() {
		List<Map<String, Object>> menuTree = menuService.getMenuTree();
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuTree);
	}
	
	/**
	 * @Description 添加菜单
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menu", value = "菜单实体", required = true, dataType = "Menu")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 2, message="菜单已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("menu:view")
	public ResponseData create(@RequestBody Menu menu) throws Exception {
		if (null == menu) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		menu.setCreator(SessionUtil.getSessionUser().getUserId());
		if (null == menu.getOrganizationId()) {
			menu.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int result = menuService.addMenu(menu);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else if (result == -2) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "菜单已存在", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}
	
	/**
	 * @Description 修改菜单
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menu", value = "菜单实体", required = true, dataType = "Menu")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 2, message="菜单已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("menu:view")
	public ResponseData update(@RequestBody Menu menu) throws Exception {
		if (null == menu) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = menuService.editMenu(menu);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else if (result == -2) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "菜单已存在", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}
	
	/**
	 * @Description 删除菜单
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menuId", value = "菜单ID", required = true, dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("menu:view")
	public ResponseData delete(int menuId) throws Exception {
		int result = menuService.deleteMenu(menuId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else if (result == -1) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "存在子菜单，请先删除子菜单", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
	
	/**
	 * @Description bus调用生成菜单
	 * @Author longqin
	 * @Time: 2023年11月28日
	 */
	@PostMapping("/diyTableMenu")
	public ResponseData diyTableMenu(String menuName, String menuUrl, Integer groupSeq, boolean isEdit) throws Exception {
		Menu menu = new Menu();
        menu.setMenuName(menuName);
        menu.setParentId(23); // 父级为自定义列表
        menu.setMenuUrl(menuUrl);
        menu.setGroupSeq(groupSeq);
        menu.setMenuIcon("layui-icon-form"); // 默认图标
        menu.setCreator(SessionUtil.getSessionUser().getUserId());
        menu.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = menuService.diyTableMenu(menu, isEdit);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "生成成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "生成失败", result);
		}
	}
}
