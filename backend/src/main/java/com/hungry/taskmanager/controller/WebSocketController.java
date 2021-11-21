package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.hungry.taskmanager.dto.MessageDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.component.WebSocketServer;
import com.hungry.taskmanager.utils.JWTUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/messagepush")
public class WebSocketController {
    @Resource
    WebSocketServer webSocketServer;

    @PostMapping("/p2pmessage")
    public Result<String> sendP2PMessage(@RequestBody MessageDTO messageDTO, HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            messageDTO.setUsernameFrom(username);
            webSocketServer.onMessage(JSON.toJSONString(messageDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully send message", "");
    }

}
