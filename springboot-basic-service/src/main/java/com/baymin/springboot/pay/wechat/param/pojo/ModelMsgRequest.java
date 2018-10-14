package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ModelMsgRequest {
    @JsonProperty("touser")
    private String touser;
    @JsonProperty("data")
    private Map<String, Map<String, String>> data;
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("miniprogram")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Miniprogram miniprogram;
    @JsonProperty("url")
    private String url;
}