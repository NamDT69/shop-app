package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse implements IResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Object data;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object getData() {
        return data;
    }
}