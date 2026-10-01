
        package com.example.ecommerce;


// Import @Test.
// @Test dùng để đánh dấu một method là một test case.
import org.junit.jupiter.api.Test;

// Import @BeforeEach.
// Method có @BeforeEach sẽ chạy trước MỖI test.
import org.junit.jupiter.api.BeforeEach;

// Import @AfterEach.
// Method có @AfterEach sẽ chạy sau MỖI test.
import org.junit.jupiter.api.AfterEach;

// Import BigDecimal.
// Dùng BigDecimal để biểu diễn giá tiền.
import java.math.BigDecimal;


// Import tất cả các assertion của JUnit.
// Ví dụ: assertEquals(), assertTrue(), assertFalse(), assertThrows().
import static org.junit.jupiter.api.Assertions.*;


// Tạo class chứa các test case của ProductService.
class day_2 {


    // Tạo một biến dùng chung cho các test.
    // Kiểu dữ liệu là BigDecimal vì price trong ProductEntity của bạn cũng là BigDecimal.
    private BigDecimal price;



    // JUnit sẽ chạy method này TRƯỚC MỖI test case.
    @BeforeEach
    void setUp() {

        // Chuẩn bị dữ liệu trước khi test.
        // Như vậy mỗi test bắt đầu với price = 1000.
        price = new BigDecimal("1000");
    }

    // JUnit sẽ chạy method này SAU MỖI test case.
    @AfterEach
    void tearDown() {

        // Dọn dẹp dữ liệu sau test.
        // Ở ví dụ này việc này không thực sự cần thiết, nhưng dùng để bạn làm quen với @AfterEach.
        price = null;
    }


    @Test
    void shouldReturnCorrectPrice() {

        // Chuẩn bị giá trị mà chúng ta MONG MUỐN nhận được. Expected = 1000
        BigDecimal expectedPrice = new BigDecimal("1000");


        // Thực hiện hành động cần test.
        // lấy giá trị price đã được chuẩn bị trong @BeforeEach.
        // Actual = price = 1000
        BigDecimal actualPrice = price;

        // Kiểm tra:
        // expectedPrice có bằng actualPrice hay không?
        // Expected = 1000
        // Actual   = 1000
        // => PASS
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void shouldReturnPositivePrice() {
        // Chuẩn bị một giá sản phẩm = 1000.
        BigDecimal productPrice = new BigDecimal("1000");

        // Kiểm tra xem productPrice có lớn hơn 0 hay không.
        // compareTo() trả về:
        // Kết quả ở đây: 1000 > 0 => true
        boolean result = productPrice.compareTo(BigDecimal.ZERO) > 0;

        // assertTrue() kiểm tra kết quả phải là true.
        // result = true => PASS
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPriceIsZero() {
        // Chuẩn bị giá sản phẩm = 0.
        BigDecimal productPrice = BigDecimal.ZERO;

        // Kiểm tra: 0 > 0 ?
        // Kết quả = false.
        boolean result = productPrice.compareTo(BigDecimal.ZERO) > 0;

        // assertFalse() kiểm tra kết quả phải là false.
        // result = false => PASS
        assertFalse(result);
    }


    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {

        // Chuẩn bị một giá âm.
        // Price = -1000
        BigDecimal productPrice = new BigDecimal("-1000");


        // =========================
        // ACT + ASSERT
        // =========================

        // Chúng ta mong muốn đoạn code bên trong sẽ throw IllegalArgumentException.
        // Nếu có Exception đúng loại => PASS
        // Nếu không có Exception => FAIL
        // Nếu throw Exception khác loại => FAIL
        assertThrows(IllegalArgumentException.class, () -> {

            // Kiểm tra xem price có nhỏ hơn 0 không.
            if (productPrice.compareTo(BigDecimal.ZERO) < 0) {
                // Nếu price < 0
                // thì chủ động throw Exception.
                throw new IllegalArgumentException(
                        "Price cannot be negative"
                );
            }
        });
    }
}
