package com.hungry.taskmanager.controller;


import com.hungry.taskmanager.dto.ConfirmDTO;
import com.hungry.taskmanager.dto.InvitationDTO;
import com.hungry.taskmanager.dto.MessageDTO;
import com.hungry.taskmanager.entity.Message;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.service.MessageService;
import com.hungry.taskmanager.utils.JWTUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/message")
public class MessageController {
    @Resource
    MessageService messageService;
    @RequestMapping("/sendrequest")
    public Result<String> sendRequest(@RequestBody InvitationDTO invitationDTO, HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            int code = messageService.sendRequest(invitationDTO.setFrom(username));
            return new Result<String>(code, "successfully send request", "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }

    }
    @RequestMapping("/confirm")
    public Result<String> confirm(@RequestBody ConfirmDTO confirmDTO, HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            messageService.confirm(confirmDTO.setFrom(username));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully confirm", "");
    }

    @RequestMapping("/getmessages")
    public Result<List<MessageDTO>> getMessages(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            List<MessageDTO> data = messageService.getMessages(username);
            return new Result<List<MessageDTO>>(200, "query message successfully", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<MessageDTO>>(500, "server error", new ArrayList<>());

        }
    }
}
