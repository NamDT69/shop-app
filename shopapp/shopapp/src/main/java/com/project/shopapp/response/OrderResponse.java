package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderResponse {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("full_name")
    private String fullName;
    private String email;
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("payment_method")
    private String paymentMethod;

}
