package com.longqin.system.entity;

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
 * @since 2023-10-21
 */
@Getter
@Setter
@ApiModel(value = "Login对象", description = "")
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("账号名称")
    private String userName;

    @ApiModelProperty("密码")
    private String password;
}
