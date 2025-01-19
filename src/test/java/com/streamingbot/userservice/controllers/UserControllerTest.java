package com.streamingbot.userservice.controllers;

import com.streamingbot.userservice.models.User;
import com.streamingbot.userservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setRoles(Arrays.asList("USER", "ADMIN"));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userService.getAllUsers()).thenReturn(Flux.just(testUser));

        StepVerifier.create(userController.getAllUsers())
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(userId)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userController.getUserById(userId))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userService.createUser(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userController.createUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userService.updateUser(any(UUID.class), any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userController.updateUser(userId, testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void deleteUser_ShouldCompleteSuccessfully() {
        when(userService.deleteUser(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userController.deleteUser(userId))
                .verifyComplete();
    }
} 