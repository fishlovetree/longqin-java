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
@TableName("sys_department")
@ApiModel(value = "Department对象", description = "")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "department_id", type = IdType.AUTO)
    private Integer departmentId;

    @ApiModelProperty("部门名称")
    @TableField("department_name")
    private String departmentName;

    @ApiModelProperty("上级部门ID")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

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

    @ApiModelProperty("子部门")
 	@TableField(exist = false)
 	private List<Department> children;
}
