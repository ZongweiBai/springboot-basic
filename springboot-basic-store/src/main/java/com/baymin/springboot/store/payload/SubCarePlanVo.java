package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel(description = "陪护项目子项")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCarePlanVo implements Serializable {

    private static final long serialVersionUID = -8512603807984403679L;

    @ApiModelProperty(notes = "系统陪护项目模板ID")
    private String planId;

    @ApiModelProperty(notes = "陪护详情")
    private String planDesc;

}
