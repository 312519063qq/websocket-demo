package com.example.websocket.service;

import com.example.websocket.model.User;

public interface UserService {
    User getUserByToken(String token);
}
