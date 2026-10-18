package com.example.ecommerce.day_3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class test {

    // Tạo ProductFinder để test.
    private ProductFinder productFinder = new ProductFinder();

    @Test
    void shouldReturnProductWhenIdExists() {
        // Chuẩn bị ID tồn tại.
        Long id = 1L;

        // Gọi method cần test.
        String result = productFinder.findProduct(id);

        // Mong muốn kết quả là "Laptop".
        assertEquals("Laptop", result);
    }

    // =====================================================
    // CASE 2: ID không tồn tại
    // =====================================================

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Chuẩn bị ID không tồn tại.
        Long id = 99L;

        // Mong muốn findProduct() phải throw RuntimeException.
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> productFinder.findProduct(id)
                );

        // ASSERT MESSAGE: Kiểm tra message của Exception.
        assertEquals(
                "Product not found", exception.getMessage()
        );
    }
}
