package com.example.ecommerce.day_5_optimized;

import com.example.ecommerce.entity.CartEntity;
import com.example.ecommerce.entity.CartItemEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.UserEntity;

import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

import com.example.ecommerce.service.OrderService;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    // =====================================================
    // MOCKS
    // =====================================================

    // Mock các Repository để test Service mà không truy cập Database.
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


    // =====================================================
    // SERVICE
    // =====================================================

    // Tạo OrderService thật và inject các Mock Repository vào.
    @InjectMocks
    private OrderService orderService;


    // =====================================================
    // SHARED TEST DATA
    // =====================================================

    // Dữ liệu dùng chung cho nhiều test case.
    private UserEntity user;
    private CartEntity cart;

    private ProductEntity laptop;
    private ProductEntity mouse;

    private CartItemEntity laptopItem;
    private CartItemEntity mouseItem;

    private List<CartItemEntity> cartItems;
}