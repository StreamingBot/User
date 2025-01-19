package com.streamingbot.userservice.models;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserProperties() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setRoles(Arrays.asList("USER", "ADMIN"));

        assertEquals(id, user.getId());
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains("USER"));
        assertTrue(user.getRoles().contains("ADMIN"));
    }

    @Test
    void testAddRoles() {
        User user = new User();
        user.addRoles("USER");
        user.addRoles("ADMIN");

        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains("USER"));
        assertTrue(user.getRoles().contains("ADMIN"));
    }

    @Test
    void testAddRoles_WithNullInitialRoles() {
        User user = new User();
        assertNull(user.getRoles());

        user.addRoles("USER");
        
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertEquals("USER", user.getRoles().get(0));
    }
} 