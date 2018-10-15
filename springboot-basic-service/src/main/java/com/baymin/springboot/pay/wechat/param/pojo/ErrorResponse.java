package com.baymin.springboot.pay.wechat.param.pojo;

/**
 * Created by JacksonGenerator on 18-9-24.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {

    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("errmsg")
    private String errmsg;

    private Map<String, Object> extension;

    @JsonAnyGetter
    public Map<String, Object> getExtension() {
        return this.extension;
    }

    public void setExtension(Map<String, Object> extension) {
        this.extension = extension;
    }

    @JsonAnySetter
    public void setExtension(String name, Object value) {
        if (this.extension == null) {
            this.extension = new HashMap();
        }

        this.extension.put(name, value);
    }
}