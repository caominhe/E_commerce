package com.example.ecommerce.day_6.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // CASE 1 — GET Product thành công
    @Test
    void shouldReturnProduct() throws Exception {

        mockMvc.perform(get("/api/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Samsung Galaxy Book 5"))
                .andExpect(jsonPath("$.price").value(1800.00))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoryId").value(2));
    }

    // CASE 2 — GET Product không tồn tại
    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {

        mockMvc.perform(get("/api/products/999999"))
                .andExpect(status().isNotFound());
    }

    // CASE 3 — POST tạo Product
    @Test
    void shouldCreateProduct() throws Exception {

        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Integration Test Product",
                                    "price": 500,
                                    "stock": 20,
                                    "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Product"))
                .andExpect(jsonPath("$.price").value(500))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    // CASE 4 — PUT update Product
    @Test
    void shouldUpdateProduct() throws Exception {

        mockMvc.perform(put("/api/products/19")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Laptop",
                                    "price": 1500,
                                    "stock": 20,
                                    "categoryId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(19))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1500))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.categoryId").value(1));
    }

    // CASE 5 — DELETE Product
    @Test
    void shouldDeleteProduct() throws Exception {

        mockMvc.perform(delete("/api/products/21"))
                .andExpect(status().isOk());
    }
}

