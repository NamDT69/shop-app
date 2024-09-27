package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderDetailDto {
    @Min(value = 1, message = "order id must be greater than 1")
    @JsonProperty("order_id")
    private Integer orderId;
    @Min(value = 1, message = "product id must be greater than 1")
    @JsonProperty("product_id")
    private Integer productId;
    @Min(value = 0, message = "price must be greater than 0")
    @Max(value = 10000000, message = "price must be less than 10000000")
    private Float price;
    @Min(value = 0, message = "product number must be greater than 0")
    @JsonProperty("number_product")
    private Integer numberOfProducts;
    @Min(value = 0, message = "total money id must be greater than 0")
    @Max(value = 10000000, message = "total money must be less than 10000000")
    @JsonProperty("total_money")
    private Float totalMoney;
}
