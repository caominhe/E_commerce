package com.example.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
}