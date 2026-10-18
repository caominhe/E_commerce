package com.example.ecommerce.day_6.WebMvcTest;

import com.example.ecommerce.controller.ProductController;
import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.math.BigDecimal;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldReturnProduct() throws Exception {

        // ARRANGE
        ProductResponse response = new ProductResponse(
                10L,
                "Laptop",
                new BigDecimal("1000"),
                10,
                1L
        );

        when(productService.getById(10L))
                .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(get("/api/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.categoryId").value(1));

        // VERIFY
        verify(productService).getById(10L);
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {

        // ARRANGE
        when(productService.getById(10L))
                .thenThrow(new ResourceNotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found"
                ));

        // ACT + ASSERT
        mockMvc.perform(get("/api/products/10"))
                .andExpect(status().isNotFound());

        // VERIFY
        verify(productService).getById(10L);
    }

    @Test
    void shouldCreateProduct() throws Exception {

        // ARRANGE
        ProductResponse response = new ProductResponse(
                10L,
                "Laptop",
                new BigDecimal("1000"),
                10,
                1L
        );

        when(productService.create(any(ProductRequest.class)))
                .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Laptop",
                        "price": 1000,
                        "stock": 10,
                        "categoryId": 1
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.categoryId").value(1));

        // VERIFY
        verify(productService).create(any(ProductRequest.class));
    }

    @Test
    void shouldUpdateProduct() throws Exception {

        // ARRANGE
        ProductResponse response = new ProductResponse(
                10L,
                "Gaming Laptop",
                new BigDecimal("1500"),
                20,
                1L
        );

        when(productService.update(
                eq(10L),
                any(ProductRequest.class)
        )).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(put("/api/products/10")
                        .contentType(APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Gaming Laptop",
                        "price": 1500,
                        "stock": 20,
                        "categoryId": 1
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(1500))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoryId").value(1));

        // VERIFY
        verify(productService).update(
                eq(10L),
                any(ProductRequest.class)
        );
    }

    @Test
    void shouldDeleteProduct() throws Exception {

        // ACT + ASSERT
        mockMvc.perform(delete("/api/products/10"))
                .andExpect(status().isOk());

        // VERIFY
        verify(productService).delete(10L);
    }

}