package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(description = "护士/护工收入排行")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRankInfoVo {

    @ApiModelProperty(notes = "我的排名")
    private int myRank;

    @ApiModelProperty(notes = "总排名")
    private List<StaffRankVo> staffRankVoList;

}
