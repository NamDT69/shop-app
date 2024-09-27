package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductImageResponse {
    @JsonProperty("image_url")
    private String imageUrl;
}
