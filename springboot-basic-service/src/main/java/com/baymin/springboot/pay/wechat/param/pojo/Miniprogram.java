package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonProperty;


public class Miniprogram {
    @JsonProperty("pagepath")
    private String pagepath;
    @JsonProperty("appid")
    private String appid;
}