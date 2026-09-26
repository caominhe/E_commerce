package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.OrderItemResponse;
import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        // 1. Tìm User
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        // 2. Tìm Cart
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cart not found"
                ));

        // 3. Lấy CartItem
        List<CartItemEntity> cartItems = cartItemRepository
                .findAll()
                .stream()
                .filter(item -> item.getCart().getId().equals(cart.getId()))
                .toList();

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cart is empty"
            );
        }

        // 4. Kiểm tra Product + Stock
        for (CartItemEntity cartItem : cartItems) {

            ProductEntity product = cartItem.getProduct();

            if (product == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                );
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Not enough stock for product: "
                                + product.getName()
                );
            }
        }

        // 5. Tính Total
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemEntity cartItem : cartItems) {

            BigDecimal itemTotal = cartItem.getProduct()
                    .getPrice()
                    .multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

            totalAmount = totalAmount.add(itemTotal);
        }

        // 6. Create Order
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");

        OrderEntity savedOrder = orderRepository.save(order);


        // 7. Create OrderItem
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CartItemEntity cartItem : cartItems) {

            ProductEntity product = cartItem.getProduct();

            OrderItemEntity orderItem = new OrderItemEntity();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        // 8. Trừ Stock
        for (CartItemEntity cartItem : cartItems) {

            ProductEntity product = cartItem.getProduct();

            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            productRepository.save(product);
        }

        // 9. Clear Cart
        cartItemRepository.deleteAll(cartItems);

        // 10. Response
        return toResponse(savedOrder, orderItems);
    }

    private OrderResponse toResponse(
            OrderEntity order,
            List<OrderItemEntity> orderItems
    ) {

        List<OrderItemResponse> items = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );
    }

    public List<OrderResponse> getAllOrders() {

        List<OrderEntity> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {

                    List<OrderItemEntity> items =
                            orderItemRepository.findAll()
                                    .stream()
                                    .filter(item ->
                                            item.getOrder().getId()
                                                    .equals(order.getId()))
                                    .toList();

                    return toResponse(order, items);
                })
                .toList();
    }

    public OrderResponse getOrderById(Long id) {

        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found"
                ));

        List<OrderItemEntity> items =
                orderItemRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getOrder().getId()
                                        .equals(order.getId()))
                        .toList();

        return toResponse(order, items);
    }
}