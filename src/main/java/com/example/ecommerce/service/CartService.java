package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.CartItemRequest;
import com.example.ecommerce.dto.response.CartItemResponse;
import com.example.ecommerce.dto.request.UpdateCartItemRequest;
import com.example.ecommerce.entity.CartEntity;
import com.example.ecommerce.entity.CartItemEntity;
import com.example.ecommerce.entity.ProductEntity;
import com.example.ecommerce.entity.UserEntity;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemResponse addItem(CartItemRequest request) {

        // 2. Tìm User
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "USER_NOT_FOUND",
                        "User not found"
                ));

        // 3. Tìm Cart
        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 4. Tìm Product
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found"
                ));

        // 5. Kiểm tra Product đã có trong Cart chưa
        CartItemEntity cartItem =
                cartItemRepository.findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                ).orElse(null);

        // 6. Create hoặc Update
        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );

        } else {

            cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }

        // 7. Save
        CartItemEntity savedItem = cartItemRepository.save(cartItem);

        // 8. Response
        return toResponse(savedItem);
    }

    private CartItemResponse toResponse(CartItemEntity cartItem) {

        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity()
        );
    }

    public List<CartItemResponse> getCart(Long userId) {

        // 1. Tìm User
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "USER_NOT_FOUND",
                        "User not found"
                ));

        // 2. Tìm Cart
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "CART_NOT_FOUND",
                        "Cart not found"
                ));

        // 3. Lấy CartItem
        List<CartItemEntity> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        // 4. Entity → Response
        return cartItems.stream()
                .map(this::toResponse)
                .toList();
    }

    public CartItemResponse updateItem(
            Long id,
            UpdateCartItemRequest request
    ) {

        // 2. Tìm CartItem thuộc User
        CartItemEntity cartItem =
                cartItemRepository.findByIdAndCartUserId(
                        id,
                        request.getUserId()
                ).orElseThrow(() -> new ResourceNotFoundException(
                        "CART_ITEM_NOT_FOUND",
                        "Cart item not found"
                ));

        // 3. Update quantity
        cartItem.setQuantity(request.getQuantity());

        // 4. Save
        CartItemEntity savedItem =
                cartItemRepository.save(cartItem);

        // 5. Response
        return toResponse(savedItem);
    }

    public void deleteItem(Long id, Long userId) {

        // 1. Tìm CartItem thuộc User
        CartItemEntity cartItem =
                cartItemRepository.findByIdAndCartUserId(
                        id,
                        userId
                ).orElseThrow(() -> new ResourceNotFoundException(
                        "CART_ITEM_NOT_FOUND",
                        "Cart item not found"
                ));

        // 2. Delete
        cartItemRepository.delete(cartItem);
    }
}