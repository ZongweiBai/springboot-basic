package com.baymin.springboot.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "微信用户信息")
@Data
public class WechatUserInfoVo {

    @ApiModelProperty(notes = "微信用户ID（非openid）")
    private String wechatId;

}
