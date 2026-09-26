package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;
}