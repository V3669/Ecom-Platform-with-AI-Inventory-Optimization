package com.ecom.order.service;

import com.ecom.order.event.OrderEvent;
import com.ecom.order.event.OrderEventPublisher;
import com.ecom.order.event.OrderEventType;
import com.ecom.order.model.Order;
import com.ecom.order.model.OrderStatus;
import com.ecom.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        
        // Publish order created event
        eventPublisher.publishOrderEvent(new OrderEvent(
            savedOrder.getId(),
            savedOrder.getUserId(),
            savedOrder.getStatus(),
            "Order created",
            null,
            OrderEventType.ORDER_CREATED
        ));
        
        return savedOrder;
    }

    @Transactional
    public Order confirmOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);
        
        eventPublisher.publishOrderEvent(new OrderEvent(
            savedOrder.getId(),
            savedOrder.getUserId(),
            savedOrder.getStatus(),
            "Order confirmed",
            null,
            OrderEventType.ORDER_CONFIRMED
        ));
        
        return savedOrder;
    }

    @Transactional
    public Order processOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        
        eventPublisher.publishOrderEvent(new OrderEvent(
            savedOrder.getId(),
            savedOrder.getUserId(),
            savedOrder.getStatus(),
            "Order cancelled",
            null,
            OrderEventType.ORDER_CANCELLED
        ));
        
        return savedOrder;
    }
} 