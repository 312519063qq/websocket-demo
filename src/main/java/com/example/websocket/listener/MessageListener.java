package com.example.websocket.listener;

import com.alibaba.fastjson.JSON;
import com.example.websocket.config.RabbitConf;
import com.example.websocket.model.MessageType;
import com.example.websocket.model.MessageVO;
import com.example.websocket.service.WebSocketService;
import com.example.websocket.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class MessageListener {

    @Autowired
    private WebSocketService webSocketService;

    @RabbitListener(queues = {RabbitConf.QUEUE_NAME}, containerFactory="rabbitListenerContainerFactory")
    public void messageHandler(String msg){
        log.info("msg:{}",msg);
        WebSocketSession session = WebSocketUtils.getSession(msg);
        if(session!=null){
            MessageVO vo = new MessageVO();
            vo.setType(MessageType.Login.name());
            vo.setMessage("支付成功");
            webSocketService.sendMessage(session, JSON.toJSONString(vo));
        }
    }
}
