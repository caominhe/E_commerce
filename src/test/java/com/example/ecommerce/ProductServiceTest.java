package com.example.ecommerce;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.ecommerce.entity.ProductEntity;


public class ProductServiceTest {

    @Test
    void shouldReturnProduct() {

        // Arrange
        ProductEntity product =
                new ProductEntity(1L, "iPhone", new BigDecimal("1000"));

        // Act
        ProductEntity result = product;

        // Assert
        assertEquals("iPhone", result.getName());
    }

    @Test
    void shouldReturnCorrectPrice() {

        // Arrange
        ProductEntity product =
                new ProductEntity(1L, "iPhone", new BigDecimal("1000"));

        // Act
        BigDecimal price = product.getPrice();

        // Assert
        assertEquals(new BigDecimal("1000"), price);
    }

}
