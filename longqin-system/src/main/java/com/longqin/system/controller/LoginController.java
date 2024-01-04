package com.longqin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.entity.Login;
import com.longqin.system.entity.Menu;
import com.longqin.system.entity.User;
import com.longqin.system.service.IMenuService;
import com.longqin.system.service.IOrganizationService;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.MD5Util;
import com.longqin.system.util.OperationLog;
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
 *  登录控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping(value = "")
@Api(tags="登录类")
public class LoginController {

	@Resource
    private IUserService userService;
	
	@Resource
    private IMenuService menuService;
	
	@Resource
    private IOrganizationService organizationService;

	/**
	 * @Description 账号登录
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
    @ApiOperation(value = "账号登录", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userName", value = "账号", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="登录成功"),
	    @ApiResponse(code = 0, message="登录失败"),
	    @ApiResponse(code = 2, message="账号名或密码错误")
	})
    @PostMapping("/login")
    @OperationLog(title = "账号登录", content = "'账号：' + #entity.getUserName() ", operationType = "5")
    public ResponseData login(@RequestBody Login entity) throws Exception {
    	if (null == entity.getUserName() || null == entity.getPassword()) {
    		return new ResponseData(ResponseEnum.BADPARAM.getCode(), "用户名和密码不能为空");
    	}
        User user = userService.getByName(entity.getUserName());
        if (null == user) {
        	return new ResponseData(ResponseEnum.ERROR.getCode(), "用户名或密码错误");
        }
        
        if (!user.getPassword().equals(MD5Util.MD5(entity.getPassword()))) {
        	return new ResponseData(2, "用户名或密码错误");
        }

		//获取用户菜单用于菜单权限判断
		List<Menu> menuList = menuService.getUserMenuList(user.getUserId(), user.getOrganizationId());
		user.setMenuList(menuList);
		// 存入缓存
		user.setPassword("");
		SessionUtil.setSessionUser(user);
		//获取用户菜单树形结构
		List<Map<String, Object>> menuTree = menuService.getUserMenuTree(user.getUserId(), user.getOrganizationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("sessionid", SessionUtil.getSessionId());
		map.put("menujson", menuTree);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "登录成功", map);
    }
    
    /**
	 * @Description 账号登出
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
    @ApiOperation(value = "账号登出", httpMethod = "POST")
	@ApiResponses({
	    @ApiResponse(code = 1, message="退出登录成功"),
	    @ApiResponse(code = 0, message="退出登录失败")
	})
    @PostMapping("/logout")
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User user = SessionUtil.getSessionUser();
		if (null != user) {
			SessionUtil.removeSessionUser();
		}
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "退出登录成功");
    }
    
    @GetMapping("/notlogon")
    public ResponseData notlogon(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new ResponseData(ResponseEnum.NOTLOGON.getCode(), "未登录", "");
    }
    
    @GetMapping("/unauth")
    public ResponseData unauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new ResponseData(ResponseEnum.UNAUTH.getCode(), "没有权限", "");
    }
}
