package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.InvitationDTO;
import org.springframework.stereotype.Service;

public interface MessageService {
    void sendRequest(InvitationDTO invitation);

}
