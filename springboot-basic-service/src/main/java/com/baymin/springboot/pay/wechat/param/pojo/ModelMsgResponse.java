package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModelMsgResponse extends ErrorResponse {
    @JsonProperty("msgid")
    private Integer msgid;
}