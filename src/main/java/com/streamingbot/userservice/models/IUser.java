package com.streamingbot.userservice.models;

import java.util.List;
import java.util.UUID;

public interface IUser {
    public UUID getId();
    public void setId(UUID id);
    public List<String> getRoles();
    public void addRoles(String role);
    public void setRoles(List<String> roles);
}
