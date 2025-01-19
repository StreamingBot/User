package com.streamingbot.userservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.streamingbot.userservice.models.UserDeletionEvent;

@Service
public class UserDeletionListener {
    private static final Logger logger = LoggerFactory.getLogger(UserDeletionListener.class);
    
    private final UserService userService;
    
    public UserDeletionListener(UserService userService) {
        this.userService = userService;
    }
    
    @RabbitListener(queues = "${rabbitmq.queue.user-deletion}")
    public void handleUserDeletion(UserDeletionEvent event) {
        logger.info("Received user deletion event for userId: {}", event.userId());
        try {
            userService.deleteByUserId(event.userId())
                .doOnSuccess(result -> logger.info("Successfully processed deletion for userId: {}", event.userId()))
                .doOnError(error -> logger.error("Failed to process user deletion for userId: {}", event.userId(), error))
                .subscribe();
        } catch (Exception e) {
            logger.error("Failed to process user deletion for userId: {}", event.userId(), e);
            throw e; // Let Spring AMQP handle the error and retry
        }
    }
} 