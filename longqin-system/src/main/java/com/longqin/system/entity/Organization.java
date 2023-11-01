package com.longqin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("sys_organization")
@ApiModel(value = "Organization对象", description = "")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "organization_id", type = IdType.AUTO)
    private Integer organizationId;

    @ApiModelProperty("公司编码")
    @TableField("organization_code")
    private String organizationCode;

    @ApiModelProperty("公司名称")
    @TableField("organization_name")
    private String organizationName;

    @ApiModelProperty("上级ID")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("状态:1-可用，0-删除")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("具体地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("联系电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("LOGO")
    @TableField("logo_path")
    private String logoPath;

    @ApiModelProperty("系统名称")
    @TableField("system_name")
    private String systemName;

    @ApiModelProperty("公司简介")
    @TableField("introduction")
    private String introduction;


}
