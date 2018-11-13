package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayRequestVo {

    private String orderId;

    private String userId;

    @ApiModelProperty(notes = "支付类型， 默认JSAPI(公众号支付), NATIVE(扫码支付)")
    private String payType = "JSAPI";

}
