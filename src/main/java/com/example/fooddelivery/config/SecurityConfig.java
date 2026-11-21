package com.example.fooddelivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .authorizeHttpRequests(auth -> auth

                // User authentication
                .requestMatchers("/users/register", "/users/login")
                .permitAll()

                // Restaurant
                .requestMatchers("/restaurants/*/menu")
                .hasAnyRole("CUSTOMER", "RESTAURANT")

                .requestMatchers("/restaurants/**")
                .hasRole("RESTAURANT")

                // Orders
                .requestMatchers("/orders")
                .hasRole("CUSTOMER")

                .requestMatchers("/orders/customer/**")
                .hasRole("CUSTOMER")

                .requestMatchers("/orders/restaurant/**")
                .hasRole("RESTAURANT")

                .requestMatchers("/orders/*/status/*")
                .hasRole("RESTAURANT")

                .requestMatchers("/orders/*")
                .hasAnyRole("CUSTOMER", "RESTAURANT")

                // Deliveries
                .requestMatchers("/deliveries/*/*")
                .hasRole("RESTAURANT")

                .requestMatchers("/deliveries/*/status/*")
                .hasRole("DELIVERY_AGENT")

                // Everything else
                .anyRequest()
                .authenticated()
            );

        return http.build();
    }
}