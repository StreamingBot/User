package com.streamingbot.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.user-deletion}")
    private String userDeletionQueue;

    @Value("${rabbitmq.exchange.user-events}")
    private String userEventsExchange;

    @Value("${rabbitmq.routing-key.user-deletion}")
    private String userDeletionRoutingKey;

    @Bean
    public Queue userDeletionQueue() {
        return QueueBuilder.durable(userDeletionQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", userDeletionQueue + ".dlq")
                .build();
    }

    @Bean
    public Queue userDeletionDlq() {
        return QueueBuilder.durable(userDeletionQueue + ".dlq").build();
    }

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(userEventsExchange);
    }

    @Bean
    public Binding userDeletionBinding(Queue userDeletionQueue, TopicExchange userEventsExchange) {
        return BindingBuilder
            .bind(userDeletionQueue)
            .to(userEventsExchange)
            .with(userDeletionRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
} 