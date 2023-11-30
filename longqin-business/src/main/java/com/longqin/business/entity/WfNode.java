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
@TableName("wf_node")
@ApiModel(value = "WfNode对象", description = "")
public class WfNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "node_id", type = IdType.AUTO)
    private Integer nodeId;

    @ApiModelProperty("节点名称")
    @TableField("node_name")
    private String nodeName;

    @ApiModelProperty("节点类型：1-普通节点，2-分流节点，3-合流节点，4-分合流点")
    @TableField("node_type")
    private Integer nodeType;

    @ApiModelProperty("节点排序")
    @TableField("group_seq")
    private Integer groupSeq;

    @ApiModelProperty("表单ID")
    @TableField("form_id")
    private Integer formId;

    @ApiModelProperty("是否虚拟节点：1-是，0-否")
    @TableField("virtual")
    private Integer virtual;

    @ApiModelProperty("是否多人协作：1-是，0-否")
    @TableField("cooperation")
    private Integer cooperation;

    @ApiModelProperty("处理部门ID")
    @TableField("department_id")
    private Integer departmentId;

    @ApiModelProperty("节点位置：x轴")
    @TableField("position_x")
    private Integer positionX;

    @ApiModelProperty("节点位置：y轴")
    @TableField("position_y")
    private Integer positionY;

    @ApiModelProperty("处理职位ID")
    @TableField("position_id")
    private Integer positionId;

    @ApiModelProperty("处理人ID")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("是否审批：1-是，0-否")
    @TableField("is_approval")
    private Integer isApproval;

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

    @ApiModelProperty("表单名称")
 	@TableField(exist = false)
    private String formName;
    
    @ApiModelProperty("前端页面id")
 	@TableField(exist = false)
    private String gid;
}
