package com.example.ecommerce.day_7;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.CategoryEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    /*@Test
    void shouldCreateProductSuccessfully() {

        // Arrange
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Laptop");

        ProductRequest request = new ProductRequest();
        request.setName("MacBook");
        request.setPrice(new BigDecimal("1500"));
        request.setStock(10);
        request.setCategoryId(1L);

        ProductEntity savedProduct = new ProductEntity();
        savedProduct.setId(10L);
        savedProduct.setName("MacBook");
        savedProduct.setPrice(new BigDecimal("1500"));
        savedProduct.setStock(10);
        savedProduct.setCategory(category);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(savedProduct);

        // Act
        ProductResponse response = productService.create(request);

        // Assert
        assertEquals(10L, response.getId());
        assertEquals("MacBook", response.getName());
        assertEquals(new BigDecimal("1500"), response.getPrice());
        assertEquals(10, response.getStock());
        assertEquals(1L, response.getCategoryId());

        // Verify
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        // Arrange
        ProductRequest request = new ProductRequest();
        request.setCategoryId(1L);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.create(request)
        );

        // Assert
        assertEquals("CATEGORY_NOT_FOUND", exception.getCode());

        // Verify
        verify(categoryRepository).findById(1L);
        verify(productRepository, never())
                .save(any(ProductEntity.class));
    }*/

    /*@Test
    void shouldReturnProductById(){

        //Arrange
        CategoryEntity category = new CategoryEntity();
        category.setId(1L);
        category.setName("Laptop");

        ProductEntity product = new ProductEntity();
        product.setId(10L);
        product.setName("Macbook");
        product.setPrice(new BigDecimal("1500"));
        product.setStock(10);
        product.setCategory(category);

        // gọi và trả về mong muốn
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        //Act gọi service thực
        ProductResponse response = productService.getById(10L);

        // Assert mong muốn test nhận

        assertEquals(10L,response.getId());
        assertEquals("Macbook",response.getName());
        assertEquals(new BigDecimal("1500"),response.getPrice());
        assertEquals(10, response.getStock());
        assertEquals(1L,response.getCategoryId());

        //Verify
        verify(productRepository).findById(10L);

    }

    @Test
    void shouldThrowExceptionWhenProductNotFound(){

        // Arrange
        when(productRepository.findById(10L))
                .thenReturn(Optional.empty());

        //Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, () -> productService.getById(10L)
        );

        //Assert
        assertEquals("PRODUCT_NOT_FOUND",exception.getCode());

        //Verify
        verify(productRepository).findById(10L);
    }*/

    /*@Test
    void shouldUpdateProductSuccessfully() {
        // Arrange
        CategoryEntity oldCategory = new CategoryEntity();
        oldCategory.setId(1L);
        oldCategory.setName("Laptop");

        CategoryEntity newCategory = new CategoryEntity();
        newCategory.setId(2L);
        newCategory.setName("Gaming");

        ProductEntity product = new ProductEntity();
        product.setId(10L);
        product.setName("MacBook");
        product.setPrice(new BigDecimal("1500"));
        product.setStock(10);
        product.setCategory(oldCategory);

        ProductRequest request = new ProductRequest();
        request.setName("Gaming Laptop");
        request.setPrice(new BigDecimal("2000"));
        request.setStock(20);
        request.setCategoryId(2L);

        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setId(10L);
        updatedProduct.setName("Gaming Laptop");
        updatedProduct.setPrice(new BigDecimal("2000"));
        updatedProduct.setStock(20);
        updatedProduct.setCategory(newCategory);

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(product)).thenReturn(updatedProduct);

        // save(product) = object mà biến product
        đang tham chiếu tới tại thời điểm Service gọi save().

        // Act
        ProductResponse response = productService.update(10L, request);

        // Assert
        assertEquals(10L, response.getId());
        assertEquals("Gaming Laptop", response.getName());
        assertEquals(new BigDecimal("2000"), response.getPrice());
        assertEquals(20, response.getStock());
        assertEquals(2L, response.getCategoryId());

        assertEquals("Gaming Laptop", product.getName());
        assertEquals(new BigDecimal("2000"), product.getPrice());
        assertEquals(20, product.getStock());
        assertEquals(newCategory, product.getCategory());

        // Verify
        verify(productRepository).findById(10L);
        verify(categoryRepository).findById(2L);
        verify(productRepository).save(product);

    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdate() {

        // Arrange
        ProductRequest request = new ProductRequest();
        request.setCategoryId(2L);

        when(productRepository.findById(10L))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(10L, request)
        );

        // Assert
        assertEquals("PRODUCT_NOT_FOUND", exception.getCode());

        // Verify
        verify(productRepository).findById(10L);
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundOnUpdate() {

        // Arrange
        ProductEntity product = new ProductEntity();
        product.setId(10L);
        product.setName("MacBook");
        product.setPrice(new BigDecimal("1500"));
        product.setStock(10);

        ProductRequest request = new ProductRequest();
        request.setName("Gaming Laptop");
        request.setPrice(new BigDecimal("2000"));
        request.setStock(20);
        request.setCategoryId(2L);

        when(productRepository.findById(10L))
                .thenReturn(Optional.of(product));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.update(10L, request)
        );

        // Assert
        assertEquals("CATEGORY_NOT_FOUND", exception.getCode());

        // Verify
        verify(productRepository).findById(10L);
        verify(categoryRepository).findById(2L);
        verify(productRepository, never())
                .save(any(ProductEntity.class));
    }
*/

    /*@Test
    void shouldDeleteProductSuccessfully() {

        // Arrange
        when(productRepository.existsById(10L))
                .thenReturn(true);

        // Act
        productService.delete(10L);

        // Verify
        verify(productRepository).existsById(10L);
        verify(productRepository).deleteById(10L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnDelete() {

        // Arrange
        when(productRepository.existsById(10L))
                .thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.delete(10L)
        );

        // Assert
        assertEquals("PRODUCT_NOT_FOUND", exception.getCode());

        // Verify
        verify(productRepository).existsById(10L);
        verify(productRepository, never())
                .deleteById(10L);
    }*/

}