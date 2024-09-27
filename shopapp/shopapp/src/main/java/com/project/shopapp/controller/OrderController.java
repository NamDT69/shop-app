package com.project.shopapp.controller;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dto.OrderDto;
import com.project.shopapp.model.Order;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.response.OrderResponse;
import com.project.shopapp.service.IOrderService;
import com.project.shopapp.utils.BindingResultUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;
    @GetMapping("")
    public ResponseEntity<String> getOrders(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok("hello");
    }
    @PostMapping("")
    public ResponseEntity<?> insertOrder (@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        OrderResponse orderResponse =mapper.map(orderService.createOrder(orderDto), OrderResponse.class);
        return ResponseEntity.ok(orderResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok().body(mapper.map(orderService.getOrder(id), OrderResponse.class));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOrderByUser(@PathVariable("id") Integer userId){
        try {
            List<OrderResponse> orderResponseList = orderService.getOrdersByUserId(userId).stream()
                    .map(order -> mapper.map(order, OrderResponse.class)).toList();
            return ResponseEntity.ok().body(orderResponseList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Integer id,@Valid @RequestBody OrderDto orderDto,  BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        try {
            Order order = orderService.updateOrder(id, orderDto);
            return ResponseEntity.ok().body(mapper.map(order, OrderResponse.class));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Integer id){
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok().body("Delete ok");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
