package com.example.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fooddelivery.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
}
