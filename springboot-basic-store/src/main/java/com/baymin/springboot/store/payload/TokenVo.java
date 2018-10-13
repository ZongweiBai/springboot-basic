package com.baymin.springboot.store.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ebaizon on 11/15/2017.
 */
@ApiModel(description = "Token信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenVo {

    @ApiModelProperty(notes = "用户ID")
    @JsonProperty("user_id")
    private String userId;

    @ApiModelProperty(notes = "access_token，用于API验证")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty(notes = "refresh_token，仅用于刷新access_token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @ApiModelProperty(notes = "access_token过期时间，单位秒")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @ApiModelProperty(notes = "token类型")
    @JsonProperty("token_type")
    private String tokenType;

    @ApiModelProperty(notes = "用户类型 U:普通用户 S：护士/护工")
    @JsonProperty("user_type")
    private String userType;

    @ApiModelProperty(notes = "微信ID，仅当用户未绑定手机号码时返回，用于后续手机绑定")
    @JsonProperty("wechat_id")
    private String wechatId;

}
