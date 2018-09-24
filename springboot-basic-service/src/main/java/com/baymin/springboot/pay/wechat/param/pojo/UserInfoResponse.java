package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse extends ErrorResponse {
    @JsonProperty("country")
    private String country;
    @JsonProperty("unionid")
    private String unionid;
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("openid")
    private String openid;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("headimgurl")
    private String headimgurl;
    @JsonProperty("privilege")
    private List<String> privilege;
}