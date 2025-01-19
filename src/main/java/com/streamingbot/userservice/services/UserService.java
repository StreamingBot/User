package com.streamingbot.userservice.services;

import com.streamingbot.userservice.models.User;
import com.streamingbot.userservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> updateUser(UUID id, User user) {
        return userRepository.existsById(id)
            .flatMap(exists -> {
                if (exists) {
                    user.setId(id);
                    return userRepository.save(user);
                }
                return Mono.empty();
            });
    }

    public Mono<Void> deleteUser(UUID id) {
        return userRepository.deleteById(id);
    }

    public Mono<Void> deleteByUserId(String userId) {
        logger.info("Deleting user data for userId: {}", userId);
        try {
            return userRepository.findById(UUID.fromString(userId))
                .flatMap(user -> {
                    logger.debug("Found user to delete: {}", user.getId());
                    return userRepository.delete(user);
                })
                .doOnSuccess(v -> logger.info("Successfully deleted user data for userId: {}", userId))
                .doOnError(e -> logger.error("Failed to delete user data for userId: {}", userId, e));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid UUID format for userId: {}", userId);
            return Mono.error(new RuntimeException("Invalid UUID format"));
        }
    }
} 