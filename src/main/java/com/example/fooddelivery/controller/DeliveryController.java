package com.example.fooddelivery.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.fooddelivery.enums.DeliveryStatus;
import com.example.fooddelivery.service.DeliveryService;

@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/deliveries/{orderId}/{deliveryAgentId}")
    public ResponseEntity<String> assignDelivery(@PathVariable Long orderId,
        @PathVariable Long deliveryAgentId) {

        deliveryService.assignDelivery(orderId, deliveryAgentId);

        return ResponseEntity.ok("Delivery assigned");
    }

    @PutMapping("/deliveries/{deliveryId}/status/{status}")
    public ResponseEntity<String> updateDeliveryStatus(
        @PathVariable Long deliveryId,
        @PathVariable DeliveryStatus status) {

        deliveryService.updateDeliveryStatus(deliveryId, status);

        return ResponseEntity.ok("Delivery status updated");
    }
}
