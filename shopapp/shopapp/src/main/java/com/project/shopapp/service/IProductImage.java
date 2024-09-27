package com.project.shopapp.service;

import com.project.shopapp.model.ProductImage;

import java.util.List;

public interface IProductImage {

    List<ProductImage> getProductImageByProductId(Integer productId);
}
