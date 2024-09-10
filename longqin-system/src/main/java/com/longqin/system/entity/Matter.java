package com.longqin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 事项
 * </p>
 *
 * @author longqin
 * @since 2024-09-04
 */
@Getter
@Setter
@TableName("sys_matter")
@ApiModel(value = "Matter对象", description = "事项")
public class Matter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("事项")
    @TableField("matter")
    private String matter;

    @ApiModelProperty("事项日期")
    @TableField("matter_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDate matterDate;

    @ApiModelProperty("事项时间")
    @TableField("matter_time")
    @DateTimeFormat(pattern = "HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalTime matterTime;

    @ApiModelProperty("1-未办，0-已办")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("创建人id")
    @TableField("creator")
    private Integer creator;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private LocalDateTime createTime;

    @ApiModelProperty("组织机构id")
    @TableField("organization_id")
    private Integer organizationId;


}
