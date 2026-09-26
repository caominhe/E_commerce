package com.example.ecommerce.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotNull
    private Long categoryId;
}