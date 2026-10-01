package com.example.ecommerce.day_5;

import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderItemResponse;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.OrderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// Cho phép JUnit sử dụng Mockito trong Test.
public class OrderTest {

    // Mock Repository → không truy cập Database thật.
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    // Tạo OrderService và tự inject các Mock Repository ở trên.
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
    // =========================
    // ARRANGE
    // =========================

        // ===== Tạo User =====
        UserEntity user = new UserEntity();
        user.setId(1L);

        // ===== Tạo Cart =====
        CartEntity cart = new CartEntity();
        cart.setId(100L);
        cart.setUser(user);

        // ===== Tạo Product 1 =====
        ProductEntity laptop = new ProductEntity();
        laptop.setId(10L);
        laptop.setName("Laptop");
        laptop.setPrice(new BigDecimal("1000"));
        laptop.setStock(10);

        // ===== Tạo Product 2 =====
        ProductEntity mouse = new ProductEntity();
        mouse.setId(20L);
        mouse.setName("Mouse");
        mouse.setPrice(new BigDecimal("50"));
        mouse.setStock(20);

        // ===== Tạo CartItem 1 =====
        // Mua 2 Laptop.
        CartItemEntity laptopItem = new CartItemEntity();
        laptopItem.setCart(cart);
        laptopItem.setProduct(laptop);
        laptopItem.setQuantity(2);

        // ===== Tạo CartItem 2 =====
        // Mua 3 Mouse.
        CartItemEntity mouseItem = new CartItemEntity();
        mouseItem.setCart(cart);
        mouseItem.setProduct(mouse);
        mouseItem.setQuantity(3);

        // Danh sách CartItem trong Cart.
        List<CartItemEntity> cartItems =
                List.of(laptopItem, mouseItem);

        // ===== Mock UserRepository =====
        // Khi Service tìm User 1 → trả về user.
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // ===== Mock CartRepository =====
        // Khi Service tìm Cart của User 1 → trả về cart.
        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        // ===== Mock CartItemRepository =====
        // Khi Service lấy tất cả CartItem → trả về cartItems.
        when(cartItemRepository.findAll())
                .thenReturn(cartItems);

        // ===== Tạo Order sau khi save =====
        OrderEntity savedOrder = new OrderEntity();
        savedOrder.setId(500L);
        savedOrder.setUser(user);
        savedOrder.setTotalAmount(new BigDecimal("2150"));
        savedOrder.setStatus("PENDING");

        // Khi Service save Order → trả về savedOrder.
        when(orderRepository.save(any(OrderEntity.class)))
                .thenReturn(savedOrder);


    // =========================
    // ACT
    // =========================

        // Tạo request đầu vào cho createOrder().
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);

        // Gọi method thật của OrderService.
        OrderResponse result = orderService.createOrder(request);

    // =========================
    // ASSERT
    // =========================

        // Kiểm tra Service có trả về Response.
        assertNotNull(result);

        // Kiểm tra thông tin Order.
        assertEquals(500L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(new BigDecimal("2150"), result.getTotalAmount());
        assertEquals("PENDING", result.getStatus());

        // Kiểm tra Order có 2 OrderItem.
        assertEquals(2, result.getItems().size());

        // Kiểm tra OrderItem đầu tiên.
        OrderItemResponse laptopResponse = result.getItems().get(0);

        assertEquals(10L, laptopResponse.getProductId());
        assertEquals("Laptop", laptopResponse.getProductName());
        assertEquals(2, laptopResponse.getQuantity());
        assertEquals(new BigDecimal("1000"), laptopResponse.getPrice());

        // Kiểm tra OrderItem thứ hai.
        OrderItemResponse mouseResponse = result.getItems().get(1);

        assertEquals(20L, mouseResponse.getProductId());
        assertEquals("Mouse", mouseResponse.getProductName());
        assertEquals(3, mouseResponse.getQuantity());
        assertEquals(new BigDecimal("50"), mouseResponse.getPrice());


        // Kiểm tra Stock đã bị trừ đúng.
        assertEquals(8, laptop.getStock());
        assertEquals(17, mouse.getStock());


    // =========================
    // VERIFY
    // =========================

        // Service phải tìm User.
        verify(userRepository).findById(1L);

        // Service phải tìm Cart của User.
        verify(cartRepository).findByUserId(1L);

        // Service phải lấy CartItems.
        verify(cartItemRepository).findAll();

        // Service phải save Order.
        verify(orderRepository).save(any(OrderEntity.class));

        // Service phải save 2 OrderItems.
        verify(orderItemRepository).saveAll(anyList());

        // Service phải save Product 2 lần để cập nhật stock.
        verify(productRepository, times(2))
                .save(any(ProductEntity.class));

        // Service phải xóa CartItems sau khi tạo Order.
        verify(cartItemRepository).deleteAll(cartItems);

    }

}

