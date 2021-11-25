package com.hungry.taskmanager.service;

public interface MailService {
//    void sendSimpleEmail(String email);
    void sendVerifyEmail(String email, String username);
    
}
