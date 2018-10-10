package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "基础服务项目信息")
@Data
public class BasicItemRequestVo implements Serializable {

    private static final long serialVersionUID = -8290863958091935900L;

    @ApiModelProperty(notes = "基础服务项目ID")
    private String id;

}
