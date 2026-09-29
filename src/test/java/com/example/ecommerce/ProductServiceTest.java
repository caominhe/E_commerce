
package com.example.ecommerce;

import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.CategoryEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void shouldReturnProductWhenIdExists() {

        // Arrange
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);

        ProductEntity product =
                new ProductEntity(1L, "iPhone", new BigDecimal("1000"));

        product.setCategory(category);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act
        ProductResponse result = productService.getById(1L);

        // Assert
        assertEquals("iPhone", result.getName());

        verify(productRepository).findById(1L);
    }


    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getById(999L)
        );

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById(999L);
    }


    @Test
    void shouldThrowExceptionWhenRepositoryFails() {

        // Arrange
        when(productRepository.findById(1L))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.getById(1L)
        );

        assertEquals("Database error", exception.getMessage());

        verify(productRepository).findById(1L);
    }
}

