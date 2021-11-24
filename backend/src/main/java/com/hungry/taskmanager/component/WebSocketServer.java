package com.hungry.taskmanager.component;


import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/messagepush/{username}")
@Component
public class WebSocketServer {

    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionMap.put(username, session);
    }

    @OnClose
    public void onClose(@PathParam("username") String username) {
        sessionMap.remove(username);
    }

    @OnMessage
    public void onMessage(String text, @PathParam("username") String username) {
        JSONObject obj = JSONObject.parseObject(text);
        // exception?
        if (obj.containsKey("heartCheck") && (Integer) obj.get("heartCheck") == 1) {
            send("1", sessionMap.get(username));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public synchronized void send(String message, Session sessionTo) {
        try {
            if (sessionTo != null) {
                sessionTo.getAsyncRemote().sendText(message);
            }else{
                System.out.println("session is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(String message, List<String> memberList){
        try {
            for (String username: memberList){
                Session session = sessionMap.get(username);
                if (session != null) {
                    session.getAsyncRemote().sendText(message);
                }else{
                    System.out.println("session is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
