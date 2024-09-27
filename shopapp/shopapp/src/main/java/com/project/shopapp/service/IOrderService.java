package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDto;
import com.project.shopapp.model.Order;

import java.util.List;

public interface IOrderService{
    Order createOrder (OrderDto orderDto);
    Order getOrder (Integer id);
    Order updateOrder(Integer id, OrderDto orderDto);
    void deleteOrder(Integer id);
    List<Order> getOrdersByUserId(Integer userId);
}
