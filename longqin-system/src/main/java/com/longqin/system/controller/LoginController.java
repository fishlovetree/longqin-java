package com.longqin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.entity.Login;
import com.longqin.system.entity.Menu;
import com.longqin.system.entity.Organization;
import com.longqin.system.entity.User;
import com.longqin.system.service.IMenuService;
import com.longqin.system.service.IOrganizationService;
import com.longqin.system.service.IUserService;
import com.longqin.system.util.MD5Util;
import com.longqin.system.util.OperationLog;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;

import cn.hutool.core.util.RandomUtil;
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
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	private static final String EMAIL_CODE = "EMAIL:";
	
	@Resource
    private JavaMailSender mailSender;
	
	// 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

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
    
    /**
	 * @Description 发送验证码
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
    @ApiOperation(value = "发送验证码", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="发送成功"),
	    @ApiResponse(code = 0, message="发送失败")
	})
    @PostMapping("/sendCode")
    public ResponseData sendCode(String email){
        if (email == null || email.equals("")){
            return new ResponseData(ResponseEnum.BADPARAM.getCode(), "邮箱不能为空");
        }
        // 从redis中查看有没有该邮箱的验证码
        String verifyCode = (String) redisTemplate.opsForValue().get(EMAIL_CODE + email);
        if (!StringUtils.isEmpty(verifyCode)) {
        	return new ResponseData(ResponseEnum.ERROR.getCode(), "验证码已发送");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(nickname + '<' + sender + '>');
        message.setTo(email);
        message.setSubject("【龙琴科技】邮箱验证码");
        // 使用 hutool-all 生成 6 位随机数验证码
        verifyCode = RandomUtil.randomNumbers(6);
        String content = "【验证码】注册邮箱验证码为" + verifyCode + "，验证码有效期为30分钟!";
        message.setText(content);
        mailSender.send(message);
        // 将该验证码存入redis
        redisTemplate.opsForValue().set(
                EMAIL_CODE + email,
                verifyCode,
                30,
                TimeUnit.MINUTES);
        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "发送成功");
    }
    
    /**
	 * @Description 用户注册
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "entity", value = "用户实体", required = true, dataType = "User")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="注册成功"),
	    @ApiResponse(code = 0, message="注册失败")
	})
    @PostMapping("/register")
//    @OperationLog(title = "账号注册", content = "'账号：' + #entity.getEmail() ", operationType = "5")
    public ResponseData register(@RequestBody User entity) throws Exception {
    	if (null == entity.getEmail()) {
    		return new ResponseData(ResponseEnum.BADPARAM.getCode(), "邮箱不能为空");
    	}

        String realCode = (String) redisTemplate.opsForValue().get(EMAIL_CODE + entity.getEmail());
        if (realCode == null || !realCode.equals(entity.getVerifyCode())){
            return new ResponseData(ResponseEnum.BADPARAM.getCode(), "验证码错误");
        }
        int count = userService.getCountByName(entity.getEmail(), 0);
        if (count > 0){
        	return new ResponseData(ResponseEnum.ERROR.getCode(), "邮箱已被注册");
        }
        Organization org = new Organization();
        org.setOrganizationName(entity.getOrganizationName());
        org.setPhone(entity.getPhone());
        organizationService.insert(org);
        entity.setUserName(entity.getEmail());
        entity.setOrganizationId(org.getOrganizationId());
        userService.insert(entity);
        if (entity.getUserId() > 0) {
        	userService.setRole(entity.getUserId(),  "1"); //默认管理员角色
        	return new ResponseData(ResponseEnum.SUCCESS.getCode(), "注册成功");
        }
        else {
        	return new ResponseData(ResponseEnum.SUCCESS.getCode(), "注册失败");
        }
    }
}
