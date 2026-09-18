package com.example.ecommerce.service;

import com.example.ecommerce.dto.CategoryRequest;
import com.example.ecommerce.dto.CategoryResponse;
import com.example.ecommerce.entity.CategoryEntity;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());

        CategoryEntity saved = categoryRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName()
        );
    }

    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    public CategoryResponse getById(Long id) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

    public CategoryResponse update(Long id, CategoryRequest request) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());

        CategoryEntity updated = categoryRepository.save(category);

        return new CategoryResponse(
                updated.getId(),
                updated.getName()
        );
    }

    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryRepository.deleteById(id);
    }
}