package com.ecom.order.event;

public enum OrderEventType {
    ORDER_CREATED,
    ORDER_CONFIRMED,
    ORDER_CANCELLED,
    STOCK_RESERVED,
    STOCK_RELEASED,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED
} 