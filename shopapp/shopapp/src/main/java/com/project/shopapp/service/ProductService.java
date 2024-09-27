package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDto;
import com.project.shopapp.dto.ProductImageDto;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.exception.InvalidParameterException;
import com.project.shopapp.model.Category;
import com.project.shopapp.model.Product;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.response.DetailProductResponse;
import com.project.shopapp.response.ProductImageResponse;
import com.project.shopapp.response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper mapper;
    @Override
    public Product createProduct(ProductDto productDto) {
        Category existingCategory = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        Product newProduct = mapper.map(productDto, Product.class);
        newProduct.setCategory(existingCategory);
        return productRepository.save(newProduct);
    }
    @Override
//    @Transactional
    public DetailProductResponse getProductById(Integer id) {
        Product product =  productRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Product do not found"));
        List<ProductImageResponse> productImages = productImageRepository.findByProductId(id).stream()
                .map(productImage -> mapper.map(productImage, ProductImageResponse.class)).toList();
        DetailProductResponse productResponse = mapper.map(product, DetailProductResponse.class);
        productResponse.setProductImageList(productImages);
        productResponse.setCategoryId(product.getCategory().getId());
        return productResponse;
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Integer categoryId, PageRequest pageRequest) {
        Page<Product> productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(product -> mapper.map(product, ProductResponse.class));
    }

    @Override
    @Transactional
    public Product updateProduct(Integer id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null){
            mapper.map(productDto, existingProduct);
            return productRepository.save(existingProduct);
        }
        throw new DataNotFoundException("Update failed");
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(product -> productRepository.delete(product));
    }

    @Override
    public Boolean existByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    @Transactional
    public ProductImage createProductImage(ProductImageDto productImageDto){
        Product existingProduct = productRepository.findById(productImageDto.getProductId())
                .orElseThrow(()-> new DataNotFoundException("product not found"));
        ProductImage productImage = mapper.map(productImageDto, ProductImage.class);
        Integer size = productImageRepository.findByProductId(productImageDto.getProductId()).size();
        if (size >= 5){
            throw new InvalidParameterException("Number of images must be less than 5");
        }
        productImage.setProduct(existingProduct);
        return productImageRepository.save(productImage);
    }


}
