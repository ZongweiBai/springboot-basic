package com.baymin.springboot.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Baymin on 2017/4/10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {

    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    protected Map<String, Object> extension;

    public ErrorInfo(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtension() {
        return this.extension;
    }

    @JsonAnySetter
    public void setExtension(String name, Object value) {
        if (this.extension == null) {
            this.extension = new HashMap();
        }

        this.extension.put(name, value);
    }

}
