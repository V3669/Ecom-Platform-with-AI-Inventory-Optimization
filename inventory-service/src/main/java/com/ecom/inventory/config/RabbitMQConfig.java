package com.ecom.inventory.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String STOCK_RESERVE_QUEUE = "stock.reserve.queue";
    public static final String STOCK_RELEASE_QUEUE = "stock.release.queue";
    public static final String LOW_STOCK_QUEUE = "low.stock.queue";
    public static final String INVENTORY_UPDATE_QUEUE = "inventory.update.queue";
    
    // Exchange names
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    
    // Routing keys
    public static final String STOCK_RESERVE_ROUTING_KEY = "stock.reserve";
    public static final String STOCK_RELEASE_ROUTING_KEY = "stock.release";
    public static final String LOW_STOCK_ROUTING_KEY = "low.stock";
    public static final String INVENTORY_UPDATE_ROUTING_KEY = "inventory.update";

    @Bean
    public Queue stockReserveQueue() {
        return new Queue(STOCK_RESERVE_QUEUE, true);
    }
    
    @Bean
    public Queue stockReleaseQueue() {
        return new Queue(STOCK_RELEASE_QUEUE, true);
    }
    
    @Bean
    public Queue lowStockQueue() {
        return new Queue(LOW_STOCK_QUEUE, true);
    }
    
    @Bean
    public Queue inventoryUpdateQueue() {
        return new Queue(INVENTORY_UPDATE_QUEUE, true);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding stockReserveBinding() {
        return BindingBuilder
                .bind(stockReserveQueue())
                .to(inventoryExchange())
                .with(STOCK_RESERVE_ROUTING_KEY);
    }
    
    @Bean
    public Binding stockReleaseBinding() {
        return BindingBuilder
                .bind(stockReleaseQueue())
                .to(inventoryExchange())
                .with(STOCK_RELEASE_ROUTING_KEY);
    }
    
    @Bean
    public Binding lowStockBinding() {
        return BindingBuilder
                .bind(lowStockQueue())
                .to(inventoryExchange())
                .with(LOW_STOCK_ROUTING_KEY);
    }
    
    @Bean
    public Binding inventoryUpdateBinding() {
        return BindingBuilder
                .bind(inventoryUpdateQueue())
                .to(inventoryExchange())
                .with(INVENTORY_UPDATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
} 