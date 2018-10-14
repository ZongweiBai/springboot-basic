package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketRequestVo {

    @ApiModelProperty(notes = "当前请求页面url,不包含#及其后面部分")
    private String url;

}
