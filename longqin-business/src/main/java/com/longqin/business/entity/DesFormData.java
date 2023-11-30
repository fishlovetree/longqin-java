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
 * @since 2023-11-02
 */
@Getter
@Setter
@ApiModel(value = "DesFormData对象", description = "")
public class DesFormData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("数据库表名")
    private String tableName;

    @ApiModelProperty("列拼接")
    private String columns;

    @ApiModelProperty("值拼接")
    private String vals;

}
