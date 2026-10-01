package com.example.ecommerce.day_3;

// Class giả lập để chúng ta luyện JUnit.
// Chưa liên quan đến ProductService thật.
class ProductFinder {

    String findProduct(Long id) {

        // Trường hợp ID = 1 → tìm thấy Product
        if (id == 1L) {
            return "Laptop";
        }

        // Các ID khác → không tìm thấy Product
        throw new RuntimeException("Product not found");
    }
}

