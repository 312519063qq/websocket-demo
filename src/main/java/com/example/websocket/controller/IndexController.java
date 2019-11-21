package com.example.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.example.websocket.config.RabbitConf;
import com.example.websocket.model.JsonResult;
import com.example.websocket.model.MessageType;
import com.example.websocket.model.MessageVO;
import com.example.websocket.model.User;
import com.example.websocket.service.UserService;
import com.example.websocket.service.WebSocketService;
import com.example.websocket.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("/index");
    }

    @GetMapping("/qrLogin")
    public JsonResult qrLogin(String sessionKey,String token){
        User user = userService.getUserByToken(token);
        //TODO 得到user登陆状态  在调取pc端登录接口    写入cookie  redis登录状态
        //省略
        // TODO 然后通知页面登录成功
        WebSocketSession session = WebSocketUtils.getSession(sessionKey);
        if(session==null){
            //TODO 如果这个机器上拿不到session 说明用户会话的不是这台  发送通知  通知到所有的机器 让别的机器去给客户端发消息
            rabbitTemplate.convertAndSend(RabbitConf.EXCHANGE,RabbitConf.QUEUE_NAME,sessionKey);
            return JsonResult.ok();
        }
        MessageVO message =new MessageVO();
        message.setType(MessageType.Login.name());
        message.setMessage("登录成功");
        webSocketService.sendMessage(session, JSON.toJSONString(message));
        return JsonResult.ok();
    }
}
