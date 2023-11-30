package com.longqin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.User;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.MD5Util;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;
import com.longqin.system.util.UploadUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  账号控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/user")
@Api(tags="账号类")
public class UserController {

	@Autowired
	IUserService userService;

	/**
	 * @Description 根据账号名获取账号
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "根据账号名获取账号信息", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userName", value = "账号名", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getUserByName")
	@RequiredPermission("user:view")
	public ResponseData getUserByName(String userName) throws Exception {
		if (null == userName) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", userName);
		}
		User user = userService.getByName(userName);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", user);
	}

	/**
	 * @Description 获取账号列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取账号列表", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "departmentId", value = "检索条件：部门ID", dataType = "String"), 
		@ApiImplicitParam(name = "nickName", value = "检索条件：昵称", dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getUserList")
	@RequiredPermission("user:view")
	public ResponseData getUserList(String departmentId, String nickName) {
		List<User> userList = userService.getPage(SessionUtil.getSessionUser().getOrganizationId(), departmentId, nickName, 0, 500);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", userList);
	}

	/**
	 * @Description 分页获取账号列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "分页获取账号", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "departmentId", value = "检索条件：部门ID", required = false, dataType = "String"), 
		@ApiImplicitParam(name = "nickName", value = "检索条件：昵称", required = false, dataType = "String"),
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getUserPage")
	@RequiredPermission("user:view")
	public ResponseData getUserPage(String departmentId, String nickName, Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<User> userList = userService.getPage(SessionUtil.getSessionUser().getOrganizationId(), departmentId, nickName, (page - 1) * size, size);
		int total = userService.getCount(SessionUtil.getSessionUser().getOrganizationId(), departmentId, nickName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}

	/**
	 * @Description 添加账号
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加账号", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "账号实体项", required = true, dataType = "User") })
	@ApiResponses({ @ApiResponse(code = 1, message = "添加成功"), @ApiResponse(code = 0, message = "添加失败"),
			@ApiResponse(code = 2, message = "账号已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("user:view")
	public ResponseData create(User user, MultipartFile file) throws Exception {
		if (null == user) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		// 上传文件
		if(null != file){
			if(file.getBytes().length > 4 * 1024 * 1024){//图标需小于4M
				return new ResponseData(ResponseEnum.ERROR.getCode(), "图标过大");
			}
			
			//判断上传文件格式
            String fileType = file.getContentType();
            if (fileType.equals("image/jpeg") || fileType.equals("image/png") || fileType.equals("image/jpeg")) {
    			String fileName = file.getOriginalFilename();
    			String avatarPath = UploadUtils.coverUpload(file.getInputStream(), fileName);
    			if(avatarPath.equals(""))
    				return new ResponseData(ResponseEnum.ERROR.getCode(), "上传LOGO失败", null);
    			else{
    				user.setAvatar(avatarPath);
    			}
            }
		}
		if (null == user.getOrganizationId())
        {
			user.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int result = userService.insert(user);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else if (result == -2){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "账号已存在", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}

	/**
	 * @Description 修改账号
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改账号", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "账号实体项", required = true, dataType = "User") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 2, message = "账号已存在"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	@RequiredPermission("user:view")
	public ResponseData update(User user, MultipartFile file) throws Exception {
		if (null == user) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		// 上传文件
		if(null != file){
			if(file.getBytes().length > 4 * 1024 * 1024){//图标需小于4M
				return new ResponseData(ResponseEnum.ERROR.getCode(), "图标过大");
			}
			
			//判断上传文件格式
            String fileType = file.getContentType();
            if (fileType.equals("image/jpeg") || fileType.equals("image/png") || fileType.equals("image/jpeg")) {
    			String fileName = file.getOriginalFilename();
    			String avatarPath = UploadUtils.coverUpload(file.getInputStream(), fileName);
    			if(avatarPath.equals(""))
    				return new ResponseData(ResponseEnum.ERROR.getCode(), "上传LOGO失败", null);
    			else{
    				user.setAvatar(avatarPath);
    			}
            }
		}
		int result = userService.update(user);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else if (result == -2){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "账号已存在", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除账号
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除账号", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("user:view")
	public ResponseData delete(int userId) throws Exception {
		int result = userService.delete(userId);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}

	/**
	 * @Description 校验密码
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "校验密码", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pwd", value = "密码", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "密码正确"), @ApiResponse(code = 0, message = "密码不正确"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/checkPassword")
	@RequiredPermission("user:view")
	public ResponseData checkPassword(int userId, String pwd) throws Exception {
		User user = userService.getById(userId);
		if (null == user || null == pwd || pwd.isEmpty()) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}

		if (!MD5Util.MD5(pwd).equals(user.getPassword())) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "密码不正确");
		}
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "密码正确");
	}

	/**
	 * @Description 修改密码
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改密码", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int"),
			@ApiImplicitParam(name = "oldPwd", value = "原密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "newPwd", value = "新密码", required = true, dataType = "String"),
			@ApiImplicitParam(name = "confirmPwd", value = "确认新密码", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 0, message = "原密码不正确"), @ApiResponse(code = 0, message = "新密码与确认新密码不一致"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/updatePassword")
	public ResponseData updatePassword(int userId, String oldPwd, String newPwd, String confirmPwd) throws Exception {
		User user = userService.getById(userId);
		if (null == user || null == oldPwd || oldPwd.isEmpty() || null == newPwd || newPwd.isEmpty()
				|| null == confirmPwd || confirmPwd.isEmpty()) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		
		if (!MD5Util.MD5(oldPwd).equals(user.getPassword())) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "原密码不正确", null);
		}
		if (!newPwd.equals(confirmPwd)) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "新密码与确认新密码不一致", null);
		}
		int result = userService.updatePassword(userId, newPwd);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 获取账号角色列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取账号角色列表", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getUserRole")
	@RequiredPermission("user:view")
	public ResponseData getUserRole(int userId) {
		List<Integer> roleList = userService.getRoles(userId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", roleList);
	}

	/**
	 * @Description 设置账号角色
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "设置账号角色", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int"),
			@ApiImplicitParam(name = "roleIdStr", value = "逗号拼接的角色ID字符串", required = true, dataType = "String") })
	@ApiResponses({ @ApiResponse(code = 1, message = "设置成功"), @ApiResponse(code = 0, message = "设置失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/updateUserRole")
	@RequiredPermission("user:view")
	public ResponseData updateUserRole(int userId, String roleIdStr) throws Exception {
		if (null == roleIdStr) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		int result = userService.setRole(userId, roleIdStr);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "设置成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "设置失败", result);
		}
	}
	
	/**
	 * @Description 获取用户职级
	 * @Author longqin
	 * @Time: 2023年11月03日
	 */
	@ApiOperation(value = "获取用户职级", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"),
		@ApiResponse(code = 3, message = "参数错误")})
	@GetMapping("/getUserPositionLevel")
	public ResponseData getUserPositionLevel(Integer userId) throws Exception {
		if (null == userId) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", userId);
		}
		int level = userService.getUserPositionLevel(userId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", level);
	}
	
	/**
	 * @Description 获取流程处理人
	 * @Author longqin
	 * @Time: 2023年11月03日
	 */
	@ApiOperation(value = "获取流程处理人", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "流程节点处理人ID", dataType = "int"),
		@ApiImplicitParam(name = "departmentId", value = "流程节点处理部门ID", dataType = "int"),
		@ApiImplicitParam(name = "positionId", value = "流程节点处理职位ID", dataType = "int"),
		@ApiImplicitParam(name = "submitterId", value = "流程提交人ID", required = true, dataType = "int")})
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"),
		@ApiResponse(code = 3, message = "参数错误")})
	@GetMapping("/getFlowHandlers")
	public ResponseData getFlowHandlers(Integer userId, Integer departmentId, Integer positionId, Integer submitterId) throws Exception {
		if (null == submitterId) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<Integer> handlers = userService.getFlowHandlers(userId, departmentId, positionId, submitterId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", handlers);
	}
}
