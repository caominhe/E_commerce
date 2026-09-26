package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.CartItemRequest;
import com.example.ecommerce.dto.response.CartItemResponse;
import com.example.ecommerce.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public CartItemResponse addItem(
            @Valid
            @RequestBody CartItemRequest request
    ) {
        return cartService.addItem(request);
    }

    @GetMapping
    public List<CartItemResponse> getCart(
            @RequestParam Long userId
    ) {
        return cartService.getCart(userId);
    }

    @PutMapping("/items/{id}")
    public CartItemResponse updateItem(
            @Valid
            @PathVariable Long id,
            @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(id, request);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        cartService.deleteItem(id, userId);
    }
}