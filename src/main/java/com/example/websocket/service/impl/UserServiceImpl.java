package com.example.websocket.service.impl;

import com.example.websocket.model.User;
import com.example.websocket.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    public static Map<String,Long> userMap =new HashMap<>();
    static {
        userMap.put("token",1L);
    }

    @Override
    public User getUserByToken(String token) {
        Long userId = userMap.get(token);
        User user =new User();
        user.setId(userId);
        user.setName("张三");
        return user;
    }
}
