package com.streamingbot.userservice.models;

public record UserDeletionEvent(
    String userId,
    String userEmail,
    long timestamp
) {} 