package com.longqin.business.entity;

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
 * @since 2023-10-31
 */
@Getter
@Setter
@ApiModel(value = "DesFormColumn对象", description = "")
public class DesFormColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字段名")
    private String columnName;

    @ApiModelProperty("数据类型")
    private String columnType;

    @ApiModelProperty("允许为空")
    private String isNull;

    @ApiModelProperty("字段描述")
    private String description;

}
