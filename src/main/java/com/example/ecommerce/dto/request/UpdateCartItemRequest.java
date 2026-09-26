package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {

    @NotNull
    private Long userId;

    @NotNull
    @Positive
    private Integer quantity;
}