package com.example.ecommerce.day_5_optimized;

import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.entity.*;

import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;

import com.example.ecommerce.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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

    // =====================================================
    // SETUP
    // =====================================================

            // Tạo lại dữ liệu dùng chung trước mỗi test để các test độc lập nhau.
            @BeforeEach
            void setUp() {
                user = createUser(1L);

                cart = createCart(100L, user);

                laptop = createProduct(
                        10L,
                        "Laptop",
                        new BigDecimal("1000"),
                        10
                );

                mouse = createProduct(
                        20L,
                        "Mouse",
                        new BigDecimal("50"),
                        20
                );

                laptopItem = createCartItem(
                        cart,
                        laptop,
                        2
                );

                mouseItem = createCartItem(
                        cart,
                        mouse,
                        3
                );

                cartItems = List.of(laptopItem, mouseItem);
            }


    // =====================================================
    // HELPER METHODS
    // =====================================================

        // Tạo các Entity mẫu để tái sử dụng trong nhiều test.
        private UserEntity createUser(Long id) {
            UserEntity user = new UserEntity();
            user.setId(id);

            return user;
        }

        private CartEntity createCart(
                Long id,
                UserEntity user
        ) {
            CartEntity cart = new CartEntity();
            cart.setId(id);
            cart.setUser(user);

            return cart;
        }

        private ProductEntity createProduct(
                Long id,
                String name,
                BigDecimal price,
                int stock
        ) {
            ProductEntity product = new ProductEntity();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);

            return product;
        }

        private CartItemEntity createCartItem(
                CartEntity cart,
                ProductEntity product,
                int quantity
        ) {
                CartItemEntity item = new CartItemEntity();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(quantity);

                return item;
        }

        private OrderEntity createSavedOrder() {
            OrderEntity order = new OrderEntity();

            order.setId(500L);
            order.setUser(user);
            order.setTotalAmount(new BigDecimal("2150"));
            order.setStatus("PENDING");

            return order;
        }

    // =====================================================
    // TEST CASES
    // =====================================================

        @Test
        void shouldCreateOrderSuccessfully() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            when(cartRepository.findByUserId(1L))
                    .thenReturn(Optional.of(cart));

            when(cartItemRepository.findAll())
                    .thenReturn(cartItems);

            OrderEntity savedOrder = createSavedOrder();

            when(orderRepository.save(any(OrderEntity.class)))
                    .thenReturn(savedOrder);


            // ACT
            OrderResponse result = orderService.createOrder(request);


            // ASSERT
            assertNotNull(result);
            assertEquals(500L, result.getId());
            assertEquals(1L, result.getUserId());
            assertEquals(
                    new BigDecimal("2150"),
                    result.getTotalAmount()
            );
            assertEquals("PENDING", result.getStatus());
            assertEquals(2, result.getItems().size());

            assertEquals(8, laptop.getStock());
            assertEquals(17, mouse.getStock());


            // VERIFY
            verify(userRepository).findById(1L);
            verify(cartRepository).findByUserId(1L);
            verify(cartItemRepository).findAll();
            verify(orderRepository).save(any(OrderEntity.class));
            verify(orderItemRepository).saveAll(anyList());
            verify(productRepository, times(2))
                    .save(any(ProductEntity.class));
            verify(cartItemRepository).deleteAll(cartItems);
        }


        @Test
        void shouldThrowExceptionWhenUserNotFound() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.empty());


            // ACT + ASSERT
            ResourceNotFoundException exception =
                    assertThrows(
                            ResourceNotFoundException.class,
                            () -> orderService.createOrder(request)
                    );

            assertEquals("USER_NOT_FOUND", exception.getCode());


            // VERIFY
            verify(userRepository).findById(1L);
            verifyNoInteractions(
                    cartRepository,
                    cartItemRepository,
                    orderRepository,
                    orderItemRepository,
                    productRepository
            );
        }


        @Test
        void shouldThrowExceptionWhenCartNotFound() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            when(cartRepository.findByUserId(1L))
                    .thenReturn(Optional.empty());


            // ACT + ASSERT
            ResourceNotFoundException exception =
                    assertThrows(
                            ResourceNotFoundException.class,
                            () -> orderService.createOrder(request)
                    );

            assertEquals("CART_NOT_FOUND", exception.getCode());


            // VERIFY
            verify(userRepository).findById(1L);
            verify(cartRepository).findByUserId(1L);

            verifyNoInteractions(
                    cartItemRepository,
                    orderRepository,
                    orderItemRepository,
                    productRepository
            );
        }


        @Test
        void shouldThrowExceptionWhenCartIsEmpty() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            when(cartRepository.findByUserId(1L))
                    .thenReturn(Optional.of(cart));

            when(cartItemRepository.findAll())
                    .thenReturn(List.of());


            // ACT + ASSERT
            BusinessException exception =
                    assertThrows(
                            BusinessException.class,
                            () -> orderService.createOrder(request)
                    );

            assertEquals("CART_EMPTY", exception.getCode());


            // VERIFY
            verify(userRepository).findById(1L);
            verify(cartRepository).findByUserId(1L);
            verify(cartItemRepository).findAll();

            verifyNoInteractions(
                    orderRepository,
                    orderItemRepository,
                    productRepository
            );
        }


        @Test
        void shouldThrowExceptionWhenProductNotFound() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            laptopItem.setProduct(null);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            when(cartRepository.findByUserId(1L))
                    .thenReturn(Optional.of(cart));

            when(cartItemRepository.findAll())
                    .thenReturn(List.of(laptopItem));


            // ACT + ASSERT
            ResourceNotFoundException exception =
                    assertThrows(
                            ResourceNotFoundException.class,
                            () -> orderService.createOrder(request)
                    );

            assertEquals("PRODUCT_NOT_FOUND", exception.getCode());


            // VERIFY
            verify(userRepository).findById(1L);
            verify(cartRepository).findByUserId(1L);
            verify(cartItemRepository).findAll();

            verifyNoInteractions(
                    orderRepository,
                    orderItemRepository,
                    productRepository
            );
        }


        @Test
        void shouldThrowExceptionWhenStockInsufficient() {

            // ARRANGE
            OrderRequest request = new OrderRequest();
            request.setUserId(1L);

            laptop.setStock(1);

            when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));

            when(cartRepository.findByUserId(1L))
                    .thenReturn(Optional.of(cart));

            when(cartItemRepository.findAll())
                    .thenReturn(List.of(laptopItem));


            // ACT + ASSERT
            BusinessException exception =
                    assertThrows(
                            BusinessException.class,
                            () -> orderService.createOrder(request)
                    );

            assertEquals(
                    "INSUFFICIENT_STOCK",
                    exception.getCode()
            );


            // VERIFY
            verify(userRepository).findById(1L);
            verify(cartRepository).findByUserId(1L);
            verify(cartItemRepository).findAll();

            verifyNoInteractions(
                    orderRepository,
                    orderItemRepository,
                    productRepository
            );
        }
}