package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderDetailResponse {
    @JsonProperty("order_id")
    private Integer orderId;
    @JsonProperty("productId")
    private Integer productId;

    private Float price;
    @JsonProperty("number_product")
    private Integer numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;
}
