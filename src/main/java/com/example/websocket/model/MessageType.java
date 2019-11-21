package com.example.websocket.model;

public enum MessageType {
    Connection(),
    CreateQRcode(),
    GetQRcode,
    Login();

    MessageType() {
    }
}
