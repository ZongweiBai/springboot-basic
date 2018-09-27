package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "护士/护工收入排行")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRankVo {

    @ApiModelProperty(notes = "护士/护工ID")
    private String staffId;

    @ApiModelProperty(notes = "总收入")
    private Double totalIncome;

    @ApiModelProperty(notes = "护士/护工手机号码")
    private String mobile;

    @ApiModelProperty(notes = "护士/护工姓名")
    private String userName;

}
