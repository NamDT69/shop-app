package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDto;
import com.project.shopapp.dto.ProductImageDto;
import com.project.shopapp.model.Product;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.response.DetailProductResponse;
import com.project.shopapp.response.ProductResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDto productDto);
    DetailProductResponse getProductById(Integer id);
    Page<ProductResponse> getAllProducts(String keyword, Integer categoryId, PageRequest pageRequest);
    Product updateProduct(Integer id, ProductDto productDto);
    void deleteProduct(Integer id);
    Boolean existByName(String name);
    ProductImage createProductImage(ProductImageDto productImageDto);

    }
