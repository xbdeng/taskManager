package com.hungry.taskmanager.component;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.dto.MessageDTO;
import com.hungry.taskmanager.entity.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
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
            System.out.println(username);
            send("1", sessionMap.get(username));
        } else if (obj.containsKey("heartCheck") && (Integer) obj.get("heartCheck") == 0) {
            Message message = JSON.parseObject(text, Message.class);
            String usernameFrom = message.getUsernameFrom();
            String usernameTo = message.getUsernameTo();
            String type = message.getContent();
            Session sessionTo = sessionMap.get(usernameTo);
            if (sessionTo != null) {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType(type).setUsernameFrom(usernameFrom).setUsernameTo(usernameTo);
                send(JSONObject.toJSONString(messageDTO), sessionTo);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void send(String message, Session sessionTo) {
        try {
            sessionTo.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
