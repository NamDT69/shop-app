package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDto {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be greater than 0")
    private Integer productId;

    @JsonProperty("image_url")
    @Size(min = 5, max= 200, message = "Image url length 5 - 200")
    private String imageUrl;

}
