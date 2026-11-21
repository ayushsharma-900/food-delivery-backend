package com.example.fooddelivery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.dto.OrderRequest;
import com.example.fooddelivery.entity.Order;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.service.OrderService;

import jakarta.validation.Valid;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<String> registerOrder(@Valid @RequestBody OrderRequest orderRequest) {

        orderService.registerOrder(orderRequest);

        return ResponseEntity.ok("Order placed successfully");
    }

    @GetMapping("/orders/customer/{customerId}")
    public List<Order> getCustomerOrders(@PathVariable Long customerId) {
        return orderService.getCustomerOrders(customerId);
    }

    @GetMapping("/orders/restaurant/{restaurantId}")
    public List<Order> getRestaurantOrders(@PathVariable Long restaurantId) {
        return orderService.getRestaurantOrders(restaurantId);
    }

    @PutMapping("/orders/{orderId}/status/{status}")
    public ResponseEntity<String> updateOrderStatus(
        @PathVariable Long orderId,
        @PathVariable OrderStatus status) {

        orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok("Order status updated");
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {

        return ResponseEntity.ok(orderService.getOrderById(orderId));
}
}
