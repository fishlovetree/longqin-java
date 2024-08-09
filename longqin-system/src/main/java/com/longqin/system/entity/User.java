package com.longqin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty("账号名称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("状态:1-可用，0-删除")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("电子邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("联系电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("部门ID")
    @TableField("department_id")
    private Integer departmentId;

    @ApiModelProperty("职位ID")
    @TableField("position_id")
    private Integer positionId;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("公司ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("部门名称")
 	@TableField(exist = false)
 	private String departmentName;
    
    @ApiModelProperty("职位名称")
 	@TableField(exist = false)
 	private String positionName;
    
    @ApiModelProperty("公司名称")
 	@TableField(exist = false)
 	private String organizationName;
    
    @ApiModelProperty("公司LOGO")
 	@TableField(exist = false)
 	private String logoPath;
    
    @ApiModelProperty("系统名称")
 	@TableField(exist = false)
 	private String systemName;
    
    @ApiModelProperty("用户菜单")
 	@TableField(exist = false)
    private List<Menu> menuList;
    
    @ApiModelProperty("验证码")
 	@TableField(exist = false)
 	private String verifyCode;
    
    @ApiModelProperty("账号角色")
 	@TableField(exist = false)
 	private String roleIds;
}
