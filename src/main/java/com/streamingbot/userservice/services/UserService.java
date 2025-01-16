package com.streamingbot.userservice.services;

import com.streamingbot.userservice.models.User;
import com.streamingbot.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class UserService {
    
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
} 