package com.project.shopapp.configuration;

import com.project.shopapp.dto.OrderDto;
import com.project.shopapp.dto.ProductDto;
import com.project.shopapp.dto.ProductImageDto;
import com.project.shopapp.dto.UserDto;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.Product;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.User;
import com.project.shopapp.response.OrderResponse;
import com.project.shopapp.response.ProductResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//config mapper dto with model
@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);


        PropertyMap<Product, ProductDto> ProductMapper = new PropertyMap<Product, ProductDto>() {
            @Override
            protected void configure() {
                map(source.getCategory().getId(), destination.getCategoryId());
            }
        };


        PropertyMap<ProductImage, ProductImageDto> ProductImageMapper = new PropertyMap<ProductImage, ProductImageDto>() {
            @Override
            protected void configure() {
                map(source.getProduct().getId(), destination.getProductId());
            }
        };

        PropertyMap<Product, ProductResponse> ProductToProductResponse = new PropertyMap<Product, ProductResponse>() {
            @Override
            protected void configure() {
                map(source.getCategory().getId(), destination.getCategoryId());
            }
        };

        PropertyMap<Order, OrderResponse> OrderToOrderResponse = new PropertyMap<Order, OrderResponse>() {
            @Override
            protected void configure() {
                map(source.getUser().getId(), destination.getUserId());
            }
        };


        modelMapper.addMappings(ProductMapper);
        modelMapper.addMappings(ProductImageMapper);
//        modelMapper.addMappings(ProductImageDtoMapper);
        modelMapper.addMappings(ProductToProductResponse);
        modelMapper.addMappings(OrderToOrderResponse);
        return modelMapper;
    }
}
