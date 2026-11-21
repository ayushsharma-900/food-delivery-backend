package com.example.fooddelivery.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.entity.MenuItem;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.exception.ResourceNotFoundException;
import com.example.fooddelivery.repository.MenuItemRepository;
import com.example.fooddelivery.repository.RestaurantRepository;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem addMenuItem(MenuItem menuItem, Long restaurantId) {

        Optional<Restaurant> result = restaurantRepository.findById(restaurantId);

        if (result.isEmpty()) {
           throw new ResourceNotFoundException("Restaurant doesn't exist");
        }
        Restaurant restaurant = result.get();
        menuItem.setRestaurant(restaurant);

        return menuItemRepository.save(menuItem);
    }
}
