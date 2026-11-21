package com.example.fooddelivery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.entity.MenuItem;
import com.example.fooddelivery.service.MenuItemService;

@RestController
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<MenuItem> addMenuItem(
            @RequestBody MenuItem menuItem,
            @PathVariable Long restaurantId) {

        MenuItem savedMenuItem = menuItemService.addMenuItem(menuItem, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
    }
}
