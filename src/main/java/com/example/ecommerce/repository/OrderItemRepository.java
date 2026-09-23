package com.example.ecommerce.repository;

import com.example.ecommerce.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItemEntity, Long> {
}