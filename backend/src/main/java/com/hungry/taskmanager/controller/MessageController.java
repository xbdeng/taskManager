package com.hungry.taskmanager.controller;


import com.hungry.taskmanager.dto.InvitationDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.utils.JWTUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/message")
public class MessageController {
    @RequestMapping("/sendrequest")
    public Result<String> sendRequest(@RequestBody InvitationDTO invitationDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        return null;
    }
}
