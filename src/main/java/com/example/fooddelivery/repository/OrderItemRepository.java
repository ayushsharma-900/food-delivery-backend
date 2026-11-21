package com.example.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fooddelivery.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
