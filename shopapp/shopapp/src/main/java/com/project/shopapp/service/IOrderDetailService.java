package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDetailDto;
import com.project.shopapp.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDto orderDetailDto);

    OrderDetail updateOrderDetail(Integer id, OrderDetailDto orderDetailDto);
    OrderDetail findOrderDetail(Integer id);
    List<OrderDetail> findByOrderId(Integer orderId);
    void deleteOrderDetail(Integer id);
}
