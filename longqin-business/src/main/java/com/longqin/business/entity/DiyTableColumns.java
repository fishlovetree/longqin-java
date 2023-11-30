package com.longqin.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("diy_table_columns")
@ApiModel(value = "DiyTableColumns对象", description = "")
public class DiyTableColumns implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("列表ID")
    @TableField("table_id")
    private Integer tableId;

    @ApiModelProperty("列表字段")
    @TableField("table_column")
    private String tableColumn;

    @ApiModelProperty("字段名称")
    @TableField("column_name")
    private String columnName;

    @ApiModelProperty("字段排序")
    @TableField("column_index")
    private Integer columnIndex;

    @ApiModelProperty("字段宽度")
    @TableField("width")
    private Integer width;

    @ApiModelProperty("是否排序：0-不排序，1-升序，2-降序")
    @TableField("order_by")
    private Integer orderBy;

    @ApiModelProperty("是否搜索：0-否，1-等于，2-模糊查询，3-介于")
    @TableField("search_type")
    private Integer searchType;

    @ApiModelProperty("公式：0-否，1-加，2-减，3-乘，4-除，5-拼接")
    @TableField("formula")
    private Integer formula;

    @ApiModelProperty("公式值")
    @TableField("formula_value")
    private String formulaValue;

    @ApiModelProperty("字段字符类型")
    @TableField("column_type")
    private String columnType;


}
