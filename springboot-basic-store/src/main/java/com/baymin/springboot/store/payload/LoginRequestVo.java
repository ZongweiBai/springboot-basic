package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "登录信息")
@Data
public class LoginRequestVo {

    @ApiModelProperty(notes = "手机号码")
    String account;

    @ApiModelProperty(notes = "验证码")
    String smsCode;

    @ApiModelProperty(notes = "微信ID")
    String wechatId;

}
