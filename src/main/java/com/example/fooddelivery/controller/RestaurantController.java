package com.example.fooddelivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.entity.MenuItem;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.service.RestaurantService;


@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

   @PostMapping("/restaurants/owner/{ownerId}")
    public ResponseEntity<Restaurant> registerRestaurant(
        @RequestBody Restaurant restaurant,
        @PathVariable Long ownerId) {

    Restaurant savedRestaurant =
            restaurantService.registerRestaurant(restaurant, ownerId);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<List<MenuItem>> getMenu(
        @PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.getMenu(restaurantId));
    }
}
