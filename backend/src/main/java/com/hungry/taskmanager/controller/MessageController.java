package com.hungry.taskmanager.controller;


import com.hungry.taskmanager.dto.InvitationDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.service.MessageService;
import com.hungry.taskmanager.utils.JWTUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/message")
public class MessageController {
    @Resource
    MessageService messageService;
    @RequestMapping("/sendrequest")
    public Result<String> sendRequest(@RequestBody InvitationDTO invitationDTO, HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            messageService.sendRequest(invitationDTO.setUsernameFrom(username));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully send request", "");
    }
}
