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
@TableName("wf_step")
@ApiModel(value = "WfStep对象", description = "")
public class WfStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "step_id", type = IdType.AUTO)
    private Integer stepId;

    @ApiModelProperty("实例ID")
    @TableField("work_id")
    private Integer workId;

    @ApiModelProperty("节点ID")
    @TableField("node_id")
    private Integer nodeId;

    @ApiModelProperty("工作ID")
    @TableField("process_id")
    private Integer processId;

    @ApiModelProperty("动作：1-前进，2-跳转，3-转办")
    @TableField("action")
    private Integer action;

    @ApiModelProperty("跳转原因")
    @TableField("reason")
    private String reason;

    @ApiModelProperty("处理人")
    @TableField("submitter")
    private Integer submitter;

    @ApiModelProperty("处理时间")
    @TableField("submit_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime submitTime;

    @ApiModelProperty("组织机构ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("节点名称")
 	@TableField(exist = false)
    private String nodeName;
    
    @ApiModelProperty("处理人姓名")
 	@TableField(exist = false)
    private String submitterName;
    
    @ApiModelProperty("工作开始时间")
 	@TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime beginTime;
    
    @ApiModelProperty("停留时间")
 	@TableField(exist = false)
    private String stayTime;

}
