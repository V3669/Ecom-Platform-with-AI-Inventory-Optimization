package com.ecom.gateway.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String message = "An unexpected error occurred";
            
            if (ex instanceof ResponseStatusException) {
                status = ((ResponseStatusException) ex).getStatusCode();
                message = ex.getMessage();
            }
            
            response.setStatusCode(status);
            
            String errorJson = String.format("{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                    status.value(), status.getReasonPhrase(), message);
            
            DataBuffer buffer = response.bufferFactory().wrap(
                    errorJson.getBytes(StandardCharsets.UTF_8));
            
            return response.writeWith(Mono.just(buffer));
        };
    }
} 