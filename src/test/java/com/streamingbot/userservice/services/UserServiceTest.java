package com.streamingbot.userservice.services;

import com.streamingbot.userservice.models.User;
import com.streamingbot.userservice.repositories.UserRepository;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
        when(userRepository.findAll()).thenReturn(Flux.just(testUser));

        StepVerifier.create(userService.getAllUsers())
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.getUserById(userId))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.createUser(testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        when(userRepository.existsById(userId)).thenReturn(Mono.just(true));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(testUser));

        StepVerifier.create(userService.updateUser(userId, testUser))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.existsById(userId)).thenReturn(Mono.just(false));

        StepVerifier.create(userService.updateUser(userId, testUser))
                .verifyComplete();
    }

    @Test
    void deleteUser_ShouldCompleteSuccessfully() {
        when(userRepository.deleteById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUser(userId))
                .verifyComplete();
    }

    @Test
    void deleteByUserId_WithValidUUID_ShouldCompleteSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Mono.just(testUser));
        when(userRepository.delete(testUser)).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteByUserId(userId.toString()))
                .verifyComplete();
    }

    @Test
    void deleteByUserId_WithInvalidUUID_ShouldReturnError() {
        StepVerifier.create(userService.deleteByUserId("invalid-uuid"))
                .expectError(RuntimeException.class)
                .verify();
    }
} 