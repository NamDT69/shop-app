package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@NoArgsConstructor
public class ProductDto {
    @NotEmpty(message = "Name not blank")
    @Size(min = 2, max = 200, message = "Name must be required character range of 2 - 200")
    @JsonProperty(value = "name")
    private String name;
    @Min(value = 0, message = "Price must be required greater than 0")
    @Max(value = 100000000, message = "Price must be required less than 100 000 000")
    @JsonProperty("price")
    private Float price;
    @JsonProperty("thumbnail")
    private String thumbnailUrl;
    @JsonProperty("description")
    private String description;
    @JsonProperty("category_id")
    private Integer categoryId;
    @JsonProperty("images")
    private List<MultipartFile> images;
}
