package com.example.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fooddelivery.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
