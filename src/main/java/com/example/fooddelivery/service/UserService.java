package com.example.fooddelivery.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.dto.LoginRequest;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.exception.ConflictException;
import com.example.fooddelivery.exception.ResourceNotFoundException;
import com.example.fooddelivery.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already registered");
        }
        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Optional<User> result = userRepository.findByEmail(request.getEmail());

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("User doesn't exist");
        }

        User user = result.get();

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(user);
    }
}
