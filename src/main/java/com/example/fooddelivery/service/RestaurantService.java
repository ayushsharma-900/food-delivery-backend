package com.example.fooddelivery.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.entity.MenuItem;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.Role;
import com.example.fooddelivery.exception.ResourceNotFoundException;
import com.example.fooddelivery.repository.MenuItemRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;

@Service
public class RestaurantService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;


    public RestaurantService(UserRepository userRepository, RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Restaurant registerRestaurant (Restaurant restaurant, Long id){

        Optional<User> result = userRepository.findById(id);
        if(result.isEmpty()){
            throw new ResourceNotFoundException("Owner doesn't exist");
        }
        User user = result.get();
        if (user.getRole() != Role.RESTAURANT) {
            throw new ResourceNotFoundException("User is not a restaurant owner");
        }
        restaurant.setOwner(user);

        return restaurantRepository.save(restaurant);      

    }

    public List<MenuItem> getMenu(Long restaurantId) {

        restaurantRepository.findById(restaurantId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Restaurant doesn't exist"));

        return menuItemRepository.findByRestaurantId(restaurantId);
    }


}
