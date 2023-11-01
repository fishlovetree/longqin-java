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
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty("菜单名称")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty("菜单路径")
    @TableField("menu_url")
    private String menuUrl;

    @ApiModelProperty("上级部门ID")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty("菜单排序")
    @TableField("group_seq")
    private Integer groupSeq;

    @ApiModelProperty("菜单图标")
    @TableField("menu_icon")
    private String menuIcon;

    @ApiModelProperty("控制器名")
    @TableField("controller")
    private String controller;

    @ApiModelProperty("方法名")
    @TableField("action")
    private String action;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("组织机构ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("状态:1-可用，0-删除")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("操作人")
    @TableField("creator")
    private Integer creator;

    @ApiModelProperty("子菜单")
 	@TableField(exist = false)
 	private List<Menu> children;
}
