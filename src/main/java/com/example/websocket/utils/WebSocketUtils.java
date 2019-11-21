package com.example.websocket.utils;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {
    /**
     * 存放session的集合
     * 对应uuid
     */
    public static Map<WebSocketSession, String> map = new ConcurrentHashMap<>();
    /**
     * map的反转集合，为了方便，多存储一个集合，MapUtils内有反转方法，可自行了解
     */
    public static Map<String, WebSocketSession> revertMap = new ConcurrentHashMap<String, WebSocketSession>();

    /**
     * 加入新链接
     * @param session
     */
    public static void onlinePerson(WebSocketSession session,String var) {
        if (map.get(session) != null) {
            return;
        }
        map.put(session, var);
        revertMap.put(var, session);
    }
    /**
     * 链接退出
     * @param session
     */
    public static void offlinePerson(WebSocketSession session) {
        revertMap.remove(map.get(session));
        map.remove(session);
    }
    /**
     * 通过uuid查询session
     */
    public static WebSocketSession getSession(String key) {
        return revertMap.get(key);
    }
    /**
     * 通过session获取uuid
     */
//    public static String getUUid(Session session) {
//        return MapUtils.getString(map, session);
//    }
    /**
     * 向session发送信息
     * @param message
     * @throws IOException
     */
    public static void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }
    /**
     * 清理无效的链接，可自行决定执行时机
     */
    public static void clearSession() {
        for (Map.Entry<WebSocketSession, String> obj : map.entrySet()) {
            WebSocketSession session = obj.getKey();
            if (!session.isOpen()) {
                revertMap.remove(obj.getValue());
                map.remove(session);
            }
        }
    }
}
