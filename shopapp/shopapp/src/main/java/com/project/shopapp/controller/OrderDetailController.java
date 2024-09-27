package com.project.shopapp.controller;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dto.OrderDetailDto;
import com.project.shopapp.model.OrderDetail;
import com.project.shopapp.response.BaseResponse;
import com.project.shopapp.response.OrderDetailResponse;
import com.project.shopapp.service.IOrderDetailService;
import com.project.shopapp.utils.BindingResultUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;
    @GetMapping("")
    public ResponseEntity<String> getOrderDetails(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        return ResponseEntity.ok("hello");
    }
    @PostMapping("")
    public ResponseEntity<?> insertOrderDetail (@RequestBody @Valid OrderDetailDto orderDetailDto, BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        try{
            OrderDetailResponse orderDetailResponse = mapper.map(orderDetailService.createOrderDetail(orderDetailDto), OrderDetailResponse.class);
            return ResponseEntity.ok().body(orderDetailResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Integer id){
        try {
            return ResponseEntity.ok().body(mapper.map(orderDetailService.findOrderDetail(id), OrderDetailResponse.class));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderByUser(@PathVariable("id") Integer orderId){
        try {
            List<OrderDetailResponse> orderResponseList = orderDetailService.findByOrderId(orderId).stream()
                    .map(order -> mapper.map(order, OrderDetailResponse.class)).toList();
            return ResponseEntity.ok().body(orderResponseList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Integer id, @Valid @RequestBody OrderDetailDto orderDetailDto, BindingResult bindingResult){
        ResponseEntity<BaseResponse> errorMsg = BindingResultUtils.getResponseEntity(localizationUtils, bindingResult);
        if (errorMsg != null) return errorMsg;
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDto);
            return ResponseEntity.ok().body(mapper.map(orderDetail, OrderDetailResponse.class));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Integer id){
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok().body("Delete ok");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
