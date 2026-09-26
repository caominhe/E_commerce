package com.example.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
}