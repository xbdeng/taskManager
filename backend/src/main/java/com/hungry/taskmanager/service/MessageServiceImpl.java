package com.hungry.taskmanager.service;

import com.hungry.taskmanager.component.WebSocketServer;
import com.hungry.taskmanager.dto.InvitationDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    WebSocketServer server;


    @Override
    public void sendRequest(InvitationDTO invitation){

    }
}
