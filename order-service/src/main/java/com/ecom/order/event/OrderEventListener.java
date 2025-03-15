package com.ecom.order.event;

import com.ecom.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void handleOrderEvent(OrderEvent event) {
        log.info("Received order event: {}", event);
        
        switch (event.getEventType()) {
            case STOCK_RESERVED -> orderService.confirmOrder(event.getOrderId());
            case STOCK_RELEASED -> orderService.cancelOrder(event.getOrderId());
            case PAYMENT_COMPLETED -> orderService.processOrder(event.getOrderId());
            case PAYMENT_FAILED -> orderService.cancelOrder(event.getOrderId());
            default -> log.warn("Unhandled event type: {}", event.getEventType());
        }
    }
} 