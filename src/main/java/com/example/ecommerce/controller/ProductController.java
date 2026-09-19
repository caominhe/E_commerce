package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse create(
            @RequestBody ProductRequest request) {

        return productService.create(request);
    }

    @GetMapping
    public Page<ProductResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return productService.search(
                name,
                categoryId,
                minPrice,
                maxPrice,
                page,
                size,
                sortBy,
                direction
        );
    }

    @GetMapping("/{id}")
    public ProductResponse getById(
            @PathVariable Long id) {

        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {

        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}