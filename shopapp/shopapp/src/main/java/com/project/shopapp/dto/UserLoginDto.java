package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginDto {
    @jakarta.validation.constraints.Email
    @NotBlank
    @JsonProperty(value = "email")
    private String Email;
    @NotBlank
    private String password;
}
