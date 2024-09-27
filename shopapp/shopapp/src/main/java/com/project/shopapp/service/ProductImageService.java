package com.project.shopapp.service;

import com.project.shopapp.model.ProductImage;
import com.project.shopapp.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImage{
    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getProductImageByProductId(Integer productId) {
        return productImageRepository.findByProductId(productId);
    }
}
