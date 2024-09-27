package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;

}
