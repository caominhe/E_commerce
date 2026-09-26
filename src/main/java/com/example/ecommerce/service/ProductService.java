package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.CategoryEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import com.example.ecommerce.exception.ResourceNotFoundException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse create(ProductRequest request) {

        CategoryEntity category = categoryRepository.findById(
                request.getCategoryId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "CATEGORY_NOT_FOUND",
                "Category not found"
        ));

        ProductEntity product = new ProductEntity();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        ProductEntity saved = productRepository.save(product);

        return toResponse(saved);
    }

    public ProductResponse getById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found"
                ));

        return toResponse(product);
    }

    public ProductResponse update(
            Long id,
            ProductRequest request) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found"
                ));

        CategoryEntity category = categoryRepository.findById(
                request.getCategoryId()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "CATEGORY_NOT_FOUND",
                "Category not found"
        ));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        ProductEntity updated = productRepository.save(product);

        return toResponse(updated);
    }

    public void delete(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "PRODUCT_NOT_FOUND",
                    "Product not found"
            );
        }

        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(ProductEntity product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId()
        );
    }

    public Page<ProductResponse> search(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductEntity> products =
                productRepository.searchProducts(
                        name,
                        categoryId,
                        minPrice,
                        maxPrice,
                        pageable
                );

        return products.map(this::toResponse);
    }
}