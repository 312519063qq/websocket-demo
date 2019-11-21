package com.example.websocket.service;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketService {
    public void sendMessage(WebSocketSession socketSession, String message);
}
