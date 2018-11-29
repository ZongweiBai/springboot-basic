package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditOrderRequestVo {

    private String orderId;

    @ApiModelProperty(notes = "开始服务日期")
    private Long serviceStartDate;

    @ApiModelProperty(notes = "结束服务日期")
    private Long serviceEndDate;

    @ApiModelProperty(notes = "服务时长（居家或者陪护）")
    private Double serviceDuration;

    private Double totalFee;

}
