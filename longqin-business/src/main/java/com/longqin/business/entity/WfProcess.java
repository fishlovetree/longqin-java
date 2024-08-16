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
@TableName("wf_process")
@ApiModel(value = "WfProcess对象", description = "")
public class WfProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "process_id", type = IdType.AUTO)
    private Integer processId;

    @ApiModelProperty("实例ID")
    @TableField("work_id")
    private Integer workId;

    @ApiModelProperty("节点ID")
    @TableField("node_id")
    private Integer nodeId;

    @ApiModelProperty("连线ID")
    @TableField("link_id")
    private Integer linkId;

    @ApiModelProperty("处理人")
    @TableField("sending_to")
    private Integer sendingTo;

    @ApiModelProperty("1-主送，2-抄送")
    @TableField("process_type")
    private Integer processType;

    @ApiModelProperty("1-未读，2-已读")
    @TableField("flag")
    private Integer flag;

    @ApiModelProperty("提交人")
    @TableField("submitter")
    private Integer submitter;

    @ApiModelProperty("提交时间")
    @TableField("submit_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime submitTime;

    @ApiModelProperty("组织机构ID")
    @TableField("organization_id")
    private Integer organizationId;

    @ApiModelProperty("1-运行中，0-关闭，2-就绪")
    @TableField("status")
    private Integer status;
    
    @ApiModelProperty("流程ID")
 	@TableField(exist = false)
    private Integer flowId;
    
    @ApiModelProperty("流程名称")
 	@TableField(exist = false)
    private String flowName;
    
    @ApiModelProperty("节点名称")
 	@TableField(exist = false)
    private String nodeName;
    
    @ApiModelProperty("创建人")
    @TableField(exist = false)
    private Integer creator;
    
    @ApiModelProperty("创建人名称")
 	@TableField(exist = false)
    private String creatorName;
    
    @ApiModelProperty("创建人部门")
 	@TableField(exist = false)
    private String departmentName;
    
    @ApiModelProperty("表单数据ID")
 	@TableField(exist = false)
    private Integer formDataId;
    
    @ApiModelProperty("创建时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;
    
    @ApiModelProperty("动作：1-前进，0-驳回，3-转办")
 	@TableField(exist = false)
    private Integer action;
    
    @ApiModelProperty("处理时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime dealTime;

}
