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
@TableName("sys_position")
@ApiModel(value = "Position对象", description = "")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "position_id", type = IdType.AUTO)
    private Integer positionId;

    @ApiModelProperty("职位名称")
    @TableField("position_name")
    private String positionName;

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

    @ApiModelProperty("职位等级：1-基层，2-中层，3-高层")
    @TableField("position_level")
    private Integer positionLevel;

    @ApiModelProperty("创建人")
    @TableField("creator")
    private Integer creator;

    @ApiModelProperty("职位简介")
    @TableField("description")
    private String description;

    @ApiModelProperty("组织机构ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("子职位")
 	@TableField(exist = false)
 	private List<Position> children;
}
