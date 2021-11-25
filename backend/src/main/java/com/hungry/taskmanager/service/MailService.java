package com.hungry.taskmanager.service;

import net.fortuna.ical4j.model.Calendar;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MailService {
    void sendVerifyEmail(String email, String username);

    void sendCalendar(String username, String email, Calendar calendar) throws IOException, MessagingException;

}
