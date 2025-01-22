package com.streamingbot.userservice.repositories;

import com.streamingbot.userservice.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
} 