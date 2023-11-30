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
 * @since 2023-10-21
 */
@Getter
@Setter
@TableName("sys_errorlog")
@ApiModel(value = "Errorlog对象", description = "")
public class Errorlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("操作人ID")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("操作人IP地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty("浏览器")
    @TableField("broswer")
    private String broswer;

    @ApiModelProperty("异常消息")
    @TableField("message")
    private String message;

    @ApiModelProperty("异常堆栈")
    @TableField("stacktrace")
    private String stacktrace;

    @ApiModelProperty("异常方法")
    @TableField("action")
    private String action;

    @ApiModelProperty("异常类")
    @TableField("error_class")
    private String errorClass;

    @ApiModelProperty("异常时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("操作人名称")
 	@TableField(exist = false)
 	private String nickName;
}
