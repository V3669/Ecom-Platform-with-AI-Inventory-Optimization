package com.ecom.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service-auth", r -> r
                        .path("/api/v1/auth/**")
                        .uri("http://localhost:8081"))
                .route("user-service-users", r -> r
                        .path("/api/v1/users/**")
                        .uri("http://localhost:8081"))
                
                // Inventory Service Routes
                .route("inventory-service-products", r -> r
                        .path("/api/v1/products/**")
                        .uri("http://localhost:8082"))
                .route("inventory-service-inventory", r -> r
                        .path("/api/v1/inventory/**")
                        .uri("http://localhost:8082"))
                
                // Order Service Routes
                .route("order-service", r -> r
                        .path("/api/v1/orders/**")
                        .uri("http://localhost:8083"))
                
                // AI Service Routes
                .route("ai-service", r -> r
                        .path("/api/v1/ai/**")
                        .uri("http://localhost:8084"))
                
                .build();
    }
} 