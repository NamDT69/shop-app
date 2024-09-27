package com.project.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CategoryDto {
    @NotEmpty(message = "Category not empty")
    @JsonProperty(value = "name")
    private String name;
}
