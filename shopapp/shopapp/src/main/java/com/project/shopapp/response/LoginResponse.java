package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginResponse extends BaseResponse{
    public LoginResponse(String message, String token) {
        super(message, token);
    }
}
