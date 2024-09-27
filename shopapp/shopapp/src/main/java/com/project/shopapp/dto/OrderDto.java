package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto {
    @Min(value = 1)
    @JsonProperty("user_id")
    private Integer userId;
    @NotBlank
    @JsonProperty("full_name")
    private String fullName;
    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")
    @NotBlank
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
    @NotBlank
    private String address;
    private String note;
    @Min(value = 0, message = "Price must be required greater than 0")
    @Max(value = 100000000, message = "Price must be required less than 100 000 000")
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;

}
