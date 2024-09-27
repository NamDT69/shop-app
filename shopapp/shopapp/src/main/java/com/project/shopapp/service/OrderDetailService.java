package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDetailDto;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.OrderDetail;
import com.project.shopapp.model.Product;
import com.project.shopapp.model.User;
import com.project.shopapp.repository.OrderDetailRepository;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDto orderDetailDto) {
        Order order = orderRepository.findById(orderDetailDto.getOrderId()).orElseThrow(
                ()->new DataNotFoundException("Not found order")
        );
        Product product = productRepository.findById(orderDetailDto.getProductId()).orElseThrow(
                ()->new DataNotFoundException("Not found product"));
        OrderDetail orderDetail = mapper.map(orderDetailDto, OrderDetail.class);
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        return orderDetailRepository.save(orderDetail);
    }


    @Override
    public OrderDetail updateOrderDetail(Integer id, OrderDetailDto orderDetailDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("not found order detail with id = " + id)

        );
        Order existingOrder  = orderRepository.findById(orderDetailDto.getOrderId()).orElseThrow(
                ()->new DataNotFoundException("not found order with id = " + orderDetailDto.getOrderId())
        );
        Product existingProduct = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(()-> new DataNotFoundException("product not found"));
        mapper.map(orderDetailDto, orderDetail);
        orderDetail.setProduct(existingProduct);
        orderDetail.setOrder(existingOrder);
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail findOrderDetail(Integer id) {
        return orderDetailRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("not found order detail with id = " + id)
        );
    }

    @Override
    public void deleteOrderDetail(Integer id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElse(null);
        if (orderDetail!=null){
            orderDetailRepository.delete(orderDetail);
        }
    }

    public List<OrderDetail> findByOrderId(Integer orderId){
        Order existingOrder  = orderRepository.findById(orderId).orElseThrow(
                ()->new DataNotFoundException("not found order with id = " +orderId)
        );
        return orderDetailRepository.findByOrderId(orderId);
    }
}
