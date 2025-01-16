package com.streamingbot.userservice.models;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "users")
public class User implements IUser {
    
    @Id
    private UUID id;
    
    private List<String> roles;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public void addRoles(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    @Override
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
