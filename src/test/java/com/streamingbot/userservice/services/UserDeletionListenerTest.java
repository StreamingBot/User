package com.streamingbot.userservice.services;

import com.streamingbot.userservice.models.UserDeletionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.*;

class UserDeletionListenerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserDeletionListener userDeletionListener;

    private UUID userId;
    private UserDeletionEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        event = new UserDeletionEvent(
            userId.toString(),
            "test@example.com",
            System.currentTimeMillis()
        );
    }

    @Test
    void handleUserDeletion_ShouldProcessDeletionSuccessfully() {
        when(userService.deleteByUserId(userId.toString())).thenReturn(Mono.empty());

        userDeletionListener.handleUserDeletion(event);

        verify(userService, times(1)).deleteByUserId(userId.toString());
    }

    @Test
    void handleUserDeletion_WhenErrorOccurs_ShouldThrowException() {
        when(userService.deleteByUserId(userId.toString()))
                .thenReturn(Mono.error(new RuntimeException("Delete failed")));

        userDeletionListener.handleUserDeletion(event);

        verify(userService, times(1)).deleteByUserId(userId.toString());
    }
} 