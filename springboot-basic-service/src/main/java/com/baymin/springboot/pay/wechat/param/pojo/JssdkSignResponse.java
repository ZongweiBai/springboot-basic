package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JssdkSignResponse {
    @JsonProperty("debug")
    private Boolean debug;
    @JsonProperty("url")
    private String jsapi_ticket;
    @JsonProperty("jsapi_ticket")
    private String nonceStr;
    @JsonProperty("nonceStr")
    private String url;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("signature")
    private String signature;
}