package com.ecom.order.event;

import com.ecom.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private OrderStatus status;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private OrderEventType eventType;
} 