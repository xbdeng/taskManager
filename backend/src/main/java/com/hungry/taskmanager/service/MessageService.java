package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.ConfirmDTO;
import com.hungry.taskmanager.dto.InvitationDTO;
import com.hungry.taskmanager.dto.MessageDTO;
import com.hungry.taskmanager.dto.WebSocketMessageDTO;
import com.hungry.taskmanager.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageService {
    int sendRequest(InvitationDTO invitation) throws Exception;

    void confirm(ConfirmDTO confirmDTO);

    List<MessageDTO> getMessages(String username);

    void sendMessages(WebSocketMessageDTO wsm, List<String> names, List<Message> messages);

}
