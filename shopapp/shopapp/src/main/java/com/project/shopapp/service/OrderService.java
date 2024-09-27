package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDto;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.model.Order;
import com.project.shopapp.model.OrderStatus;
import com.project.shopapp.model.User;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    @Override
    public Order createOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId()).orElseThrow(
                ()->new DataNotFoundException("User do not found")
        );
        Order order = mapper.map(orderDto, Order.class);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.from(ZonedDateTime.now()));
        order.setStatus(OrderStatus.pending);
        LocalDateTime shippingDate = order.getShippingDate() == null ? LocalDateTime.now(): order.getShippingDate();
        if (shippingDate.isBefore(LocalDateTime.now())){
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Integer id) {
        return orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("No found order")
        );
    }

    @Override
    public Order updateOrder(Integer id, OrderDto orderDto) {
        Order existingOrder  = orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("not found order with id = " + id)
        );
        User existingUser = userRepository.findById(orderDto.getUserId()).orElseThrow(
                () -> new DataNotFoundException("Not found user with id = " + orderDto.getUserId())
        );
        mapper.map(orderDto, existingOrder);
        existingOrder.setUser(existingUser);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Integer id) {
        Order existingOrder = getOrder(id);
        if (existingOrder != null){
            existingOrder.setActive(0);
            orderRepository.save(existingOrder);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()->new DataNotFoundException("no found user")
        );
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders;
    }
}
