package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(
            @Valid
            @RequestBody OrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(
            @PathVariable Long id
    ) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }
}