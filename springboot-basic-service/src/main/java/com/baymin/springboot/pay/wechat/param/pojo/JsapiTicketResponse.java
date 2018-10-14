package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsapiTicketResponse extends ErrorResponse {
    @JsonProperty("ticket")
    private String ticket;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}