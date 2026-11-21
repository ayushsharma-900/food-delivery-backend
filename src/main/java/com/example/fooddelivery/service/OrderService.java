package com.example.fooddelivery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.dto.OrderItemRequest;
import com.example.fooddelivery.dto.OrderRequest;
import com.example.fooddelivery.entity.MenuItem;
import com.example.fooddelivery.entity.Order;
import com.example.fooddelivery.entity.OrderItem;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.exception.ResourceNotFoundException;
import com.example.fooddelivery.repository.MenuItemRepository;
import com.example.fooddelivery.repository.OrderItemRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.RestaurantRepository;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import com.example.fooddelivery.enums.Role;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
            RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository,
            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }
    

    public void registerOrder(OrderRequest orderRequest){
        Optional<User> result1 = userRepository.findById(orderRequest.getCustomerId());
        if(result1.isEmpty()){
            throw new ResourceNotFoundException("Customer doesn't exist");
        }
        User customer = result1.get();
        Optional<Restaurant> result2 = restaurantRepository.findById(orderRequest.getRestaurantId());
        if(result2.isEmpty()){
            throw new ResourceNotFoundException ("Restaurant doesn't exit");
        }
        Restaurant restaurant = result2.get();

        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);
        order.setTotal(0.0);
        orderRepository.save(order);

        for(OrderItemRequest item : orderRequest.getItems()){
            Optional<MenuItem> result3 = menuItemRepository.findById(item.getMenuItemId());
            if(result3.isEmpty()){
                throw new ResourceNotFoundException("Menu item does not exit");
            }
            MenuItem menuItem = result3.get();
            if (!menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                throw new ResourceNotFoundException("Menu item does not belong to this restaurant");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getQuantity()*menuItem.getPrice());
            order.setTotal(order.getTotal()+orderItem.getPrice());
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(order);
    }
    

    public List<Order> getCustomerOrders(Long customerId) {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
             new ResourceNotFoundException("User doesn't exist"));

        if (user.getRole() != Role.CUSTOMER ||
            !user.getId().equals(customerId)) {
            throw new AccessDeniedException("You cannot view these orders");
        }
        return orderRepository.findByCustomerId(customerId);
    }


    public List<Order> getRestaurantOrders(Long restaurantId) {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new ResourceNotFoundException("User doesn't exist"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
            new ResourceNotFoundException("Restaurant doesn't exist"));

        if (user.getRole() != Role.RESTAURANT ||
            !restaurant.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot view these orders");
        }

        return orderRepository.findByRestaurantId(restaurantId);
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> result = orderRepository.findById(orderId);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Order doesn't exist");
        }

        Order order = result.get();
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                    new ResourceNotFoundException("Order doesn't exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new ResourceNotFoundException("User doesn't exist"));

        if (user.getRole() == Role.CUSTOMER) {

            if (!order.getCustomer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot view this order");
            }

        } 
        else if (user.getRole() == Role.RESTAURANT) {
            if (!order.getRestaurant().getOwner().getId().equals(user.getId())) {
                throw new AccessDeniedException("You cannot view this order");
            }
        } 
        else {
            throw new AccessDeniedException("You cannot view this order");
        }

        return order;
    }
}
