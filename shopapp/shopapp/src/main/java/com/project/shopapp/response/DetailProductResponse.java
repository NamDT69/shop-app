package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.model.ProductImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DetailProductResponse {
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
    @JsonProperty("product_images")
    private List<ProductImageResponse> productImageList;
}
