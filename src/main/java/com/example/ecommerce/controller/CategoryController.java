package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponse create(
            @RequestBody CategoryRequest request) {

        return categoryService.create(request);
    }

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(
            @PathVariable Long id) {

        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {

        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}