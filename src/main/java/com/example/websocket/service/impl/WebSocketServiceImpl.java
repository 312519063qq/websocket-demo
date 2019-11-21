package com.example.websocket.service.impl;

import com.example.websocket.service.WebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Override
    public void sendMessage(WebSocketSession socketSession, String message) {
        try {
            TextMessage textMessage = new TextMessage(message);
            socketSession.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
