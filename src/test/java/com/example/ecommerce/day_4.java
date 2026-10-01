package com.example.ecommerce;

import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.CategoryEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;

// @ExtendWith: cho phép JUnit sử dụng Mockito.
import org.junit.jupiter.api.extension.ExtendWith;

// MockitoExtension: khởi tạo @Mock và @InjectMocks.
import org.mockito.junit.jupiter.MockitoExtension;

// @Mock: tạo dependency giả.
import org.mockito.Mock;

// @InjectMocks: inject các dependency giả vào ProductService.
import org.mockito.InjectMocks;

// Optional: kết quả có thể có hoặc không có Product.
import java.util.Optional;

// BigDecimal: dùng cho price.
import java.math.BigDecimal;

// Các assertion của JUnit.
import static org.junit.jupiter.api.Assertions.*;

// when(): quy định Mock sẽ trả về gì.
import static org.mockito.Mockito.when;

// verify(): kiểm tra Mock có được gọi hay không.
import static org.mockito.Mockito.verify;

// never(): kiểm tra một method KHÔNG được gọi.
import static org.mockito.Mockito.never;


// Nói cho JUnit:
// "Class test này sử dụng Mockito."
@ExtendWith(MockitoExtension.class)
class day_4 {

    // Mock: các phụ thuộc > InjectMocks: ai cần các phụ thuộc nào //

    // Tạo ProductRepository giả.
    // Không kết nối Database. Không chạy SQL.
    @Mock
    private ProductRepository productRepository;

    // Tạo CategoryRepository giả.
    // ProductService cần dependency này, nên cũng phải tạo Mock.
    @Mock
    private CategoryRepository categoryRepository;

    // Mockito sẽ inject: các Mock được tạo vào ProductService.
    @InjectMocks
    private ProductService productService;


    // ========================================================
    // TEST 1: getById() — PRODUCT TỒN TẠI
    // ========================================================

    @Test
    void shouldReturnProductWhenIdExists() {

        // Tạo Category giả để Product có category.
        CategoryEntity category = new CategoryEntity();
        category.setId(2L);

        // Tạo Product giả.
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("1000"));
        product.setStock(10);

        // Gắn Category cho Product.
        product.setCategory(category);


        // ----------------------------------------------------
        // MOCK
        // ----------------------------------------------------

        // Khi ProductService gọi: productRepository.findById(1L)
        // Mockito không đi Database. Nó trả về Product chúng ta đã chuẩn bị.
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Gọi method thật của ProductService.
        // ProductService sẽ gọi Mock Repository.
        ProductResponse result =
                productService.getById(1L);

        // ----------------------------------------------------
        // ASSERT
        // ----------------------------------------------------

        // Kiểm tra ProductResponse không null.
        assertNotNull(result);

        // Kiểm tra ID đúng.
        assertEquals(1L, result.getId());

        // Kiểm tra name đúng.
        assertEquals("Laptop", result.getName());

        // Kiểm tra price đúng.
        assertEquals(
                new BigDecimal("1000"), result.getPrice()
        );

        // Kiểm tra stock đúng.
        assertEquals(10, result.getStock());

        // Kiểm tra categoryId đúng.
        assertEquals(2L, result.getCategoryId());


        // Kiểm tra ProductService thực sự gọi productRepository.findById(1L).
        verify(productRepository).findById(1L);
    }


    // ========================================================
    // TEST 2 getById() — PRODUCT KHÔNG TỒN TẠI
    // ========================================================

    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        // ----------------------------------------------------
        // ARRANGE + MOCK
        // ----------------------------------------------------

        // Khi ProductService gọi findById(99L), giả lập Repository trả về Optional.empty().
        // Optional.empty() = không tìm thấy Product.
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());


        // ----------------------------------------------------
        // ACT + ASSERT
        // ----------------------------------------------------

        // Gọi getById(99L).
        // ProductService sẽ chạy: .orElseThrow(...) và throw ResourceNotFoundException.
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.getById(99L)
                );

        // Kiểm tra message của Exception.
        assertEquals(
                "Product not found", exception.getMessage()
        );

        // Kiểm tra Repository đã được gọi.
        verify(productRepository) .findById(99L);
    }


    // ========================================================
    // TEST 3 delete() — PRODUCT TỒN TẠI
    // ========================================================

    @Test
    void shouldDeleteProductWhenIdExists() {
        // ----------------------------------------------------
        // ARRANGE + MOCK
        // ----------------------------------------------------

        // Giả lập: Product ID 1 tồn tại.
        // Khi Service gọi existsById(1L) → Mock trả true.
        when(productRepository.existsById(1L))
                .thenReturn(true);

        // ----------------------------------------------------
        // ACT
        // ----------------------------------------------------

        // Gọi method delete() thật.
        productService.delete(1L);

        // Product tồn tại → Service phải gọi deleteById(1L).
        verify(productRepository) .deleteById(1L);
    }


    // ========================================================
    // TEST 4 delete() — PRODUCT KHÔNG TỒN TẠI
    // ========================================================

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        // ----------------------------------------------------
        // ARRANGE + MOCK
        // ----------------------------------------------------

        // Giả lập Product ID 99 không tồn tại.
        // existsById(99L) → false.
        when(productRepository.existsById(99L))
                .thenReturn(false);

        // ----------------------------------------------------
        // ACT + ASSERT
        // ----------------------------------------------------

        // Gọi delete(99L).
        // ProductService sẽ throw: ResourceNotFoundException.
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.delete(99L)
                );

        // Kiểm tra message.
        assertEquals("Product not found", exception.getMessage()
        );

        // Repository phải được gọi để kiểm tra Product tồn tại.
        verify(productRepository).existsById(99L);

        // Product không tồn tại
        // → KHÔNG được phép gọi deleteById().
        verify(productRepository, never())
                .deleteById(99L);
    }
}

