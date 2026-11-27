package com.example.fooddelivery.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.fooddelivery.dto.LoginRequest;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.exception.ConflictException;
import com.example.fooddelivery.exception.UnauthorizedException;
import com.example.fooddelivery.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        Optional<User> result = userRepository.findByEmail(request.getEmail());

        if (result.isEmpty()) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = result.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        return jwtService.generateToken(user);
    }
}
