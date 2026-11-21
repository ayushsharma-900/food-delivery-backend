package com.example.fooddelivery.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.entity.Delivery;
import com.example.fooddelivery.entity.Order;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.DeliveryStatus;
import com.example.fooddelivery.enums.Role;
import com.example.fooddelivery.exception.ResourceNotFoundException;
import com.example.fooddelivery.repository.DeliveryRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.UserRepository;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    public DeliveryService(DeliveryRepository deliveryRepository, OrderRepository orderRepository,
            UserRepository userRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }



    public void assignDelivery(Long orderId, Long deliveryAgentId) {

        Optional<Order> result1 = orderRepository.findById(orderId);

        if (result1.isEmpty()) {
            throw new ResourceNotFoundException("Order doesn't exist");
        }

        Order order = result1.get();
        Optional<User> result2 = userRepository.findById(deliveryAgentId);

        if (result2.isEmpty()) {
            throw new ResourceNotFoundException("Delivery agent doesn't exist");
        }

        User deliveryAgent = result2.get();

        if (deliveryAgent.getRole() != Role.DELIVERY_AGENT) {
            throw new ResourceNotFoundException("User is not a delivery agent");
        }

        Delivery delivery = new Delivery();

        delivery.setOrder(order);
        delivery.setDeliveryAgent(deliveryAgent);
        delivery.setStatus(DeliveryStatus.ASSIGNED);

        deliveryRepository.save(delivery);
    }

    public void updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        Optional<Delivery> result = deliveryRepository.findById(deliveryId);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Delivery doesn't exist");
        }

        Delivery delivery = result.get();

        delivery.setStatus(status);

        deliveryRepository.save(delivery);
    }
}
