package com.project.shopapp.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ProductsResponse {
    List<ProductResponse> productResponses;
    Integer totalPage;
}
