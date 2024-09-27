package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.model.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private Float price;
    @JsonProperty("thumbnail")
    private String thumbnailUrl;
    @JsonProperty("description")
    private String description;
    @JsonProperty("category_id")
    private Integer categoryId;

}
