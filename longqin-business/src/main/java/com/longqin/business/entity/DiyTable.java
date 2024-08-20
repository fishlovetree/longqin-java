package com.longqin.business.entity;

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
 * @since 2023-11-09
 */
@Getter
@Setter
@TableName("diy_table")
@ApiModel(value = "DiyTable对象", description = "")
public class DiyTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("列表名称")
    @TableField("table_name")
    private String tableName;

    @ApiModelProperty("数据源表名")
    @TableField("data_source")
    private String dataSource;

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
    
    @ApiModelProperty("创建人名称")
 	@TableField(exist = false)
    private String creatorName;
    
    @ApiModelProperty("列表详情")
 	@TableField(exist = false)
    private String data;
    
    @ApiModelProperty("列表字段集合")
 	@TableField(exist = false)
    private List<DiyTableColumns> columns;

}
