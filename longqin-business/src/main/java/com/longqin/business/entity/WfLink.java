package com.longqin.business.entity;

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
 * @since 2023-11-01
 */
@Getter
@Setter
@TableName("wf_link")
@ApiModel(value = "WfLink对象", description = "")
public class WfLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    @ApiModelProperty("连线名称")
    @TableField("link_name")
    private String linkName;

    @ApiModelProperty("起始节点ID")
    @TableField("from_node_id")
    private Integer fromNodeId;

    @ApiModelProperty("终到节点ID")
    @TableField("to_node_id")
    private Integer toNodeId;

    @ApiModelProperty("表单ID")
    @TableField("form_id")
    private Integer formId;

    @ApiModelProperty("条件字段")
    @TableField("field")
    private String field;

    @ApiModelProperty("比较符号：><=等")
    @TableField("operator")
    private String operator;

    @ApiModelProperty("条件值")
    @TableField("operator_value")
    private String operatorValue;

    @ApiModelProperty("连线位置：x轴")
    @TableField("position_x")
    private Integer positionX;

    @ApiModelProperty("连线位置：y轴")
    @TableField("position_y")
    private Integer positionY;

    @ApiModelProperty("流程ID")
    @TableField("flow_id")
    private Integer flowId;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("创建人")
    @TableField("creator")
    private Integer creator;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("组织机构ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("状态")
    @TableField("status")
    private Integer status;


}
