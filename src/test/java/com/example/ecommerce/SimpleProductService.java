package com.example.ecommerce;

import com.example.ecommerce.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleProductService {

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

    public ProductEntity getById(ProductEntity product) {
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        return product;
    }

    @Test
    void shouldReturnProductWhenIdExists() {

        // Arrange
        ProductEntity product =
                new ProductEntity(1L, "iPhone", new BigDecimal("1000"));

        SimpleProductService service = new SimpleProductService();

        // Act
        ProductEntity result = service.getById(product);

        // Assert
        assertEquals("iPhone", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        // Arrange
        SimpleProductService service = new SimpleProductService();

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getById(null)
        );

        assertEquals("Product not found", exception.getMessage());
        System.out.println(exception.getMessage());

    }

}
