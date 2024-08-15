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
@TableName("wf_workform")
@ApiModel(value = "WfWorkform对象", description = "")
public class WfWorkform implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("实例ID")
    @TableField("work_id")
    private Integer workId;

    @ApiModelProperty("节点ID")
    @TableField("node_id")
    private Integer nodeId;

    @ApiModelProperty("工作ID")
    @TableField("process_id")
    private Integer processId;

    @ApiModelProperty("表单数据ID")
    @TableField("form_data_id")
    private Integer formDataId;

    @ApiModelProperty("数据库表名")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty("表单json")
    @TableField(exist = false)
    private String jsonData;

    @ApiModelProperty("表单名称")
    @TableField(exist = false)
    private String formName;

    @ApiModelProperty("是否审批：1-是，0-否")
    @TableField(exist = false)
    private Integer isApproval;

    @ApiModelProperty("创建人")
    @TableField(exist = false)
    private Integer creator;

    @ApiModelProperty("创建时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("组织机构ID")
    @TableField(exist = false)
    private Integer organizationId;
    
    @ApiModelProperty("提交人")
    @TableField(exist = false)
    private Integer submitter;
    
    @ApiModelProperty("提交人昵称")
    @TableField(exist = false)
    private String submitterName;

    @ApiModelProperty("提交时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime submitTime;


}
