package com.example.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.websocket.model.MessageType;
import com.example.websocket.model.MessageVO;
import com.example.websocket.service.WebSocketService;
import com.example.websocket.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    private MessageVO messageVO = new MessageVO();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("------------------------连接后--------------------------------");
        super.afterConnectionEstablished(session);
        messageVO.setType(MessageType.Connection.name());
        webSocketService.sendMessage(session,JSON.toJSONString(messageVO));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("-------------------------handleMessage-------------------------------" + message.getPayload());
        super.handleMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //TODO 把session和二维码对应的uuid 存在本地   把orderId和uuid存储在redis里 发送给前端生成二维码
        List<MessageVO> messageVOS = JSONObject.parseArray(message.getPayload(), MessageVO.class);
        MessageVO messageVO = messageVOS.get(0);
        if(messageVO.getType().equals(MessageType.CreateQRcode.name())){
            log.info("产生二维码");
//            String s = UUID.randomUUID().toString();
            String sessonKey ="uuid";
            //存储用户session会话
            WebSocketUtils.onlinePerson(session,sessonKey);
            //存储二维码 的uuid
            redisTemplate.opsForValue().set(sessonKey,"");
            messageVO.setMessage("http://127.0.0.1:8080/qrLogin?sessionKey="+sessonKey);
            messageVO.setType(MessageType.GetQRcode.name());
        }
        //二维码url
        webSocketService.sendMessage(session,JSON.toJSONString(messageVO));
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        System.out.println("---------------------------handlePongMessage-----------------------------" + message.getPayload());
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("---------------------------handleTransportError-----------------------------");
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("---------------------------连接关闭-----------------------------");
        WebSocketUtils.offlinePerson(session);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        System.out.println("----------------------------supportsPartialMessages----------------------------");
        return super.supportsPartialMessages();
    }

}
