package com.hungry.taskmanager.service;

import com.hungry.taskmanager.entity.Task;
import net.fortuna.ical4j.model.Calendar;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MailService {
    void sendVerifyEmail(String email, String username);

    void sendCalendar(String username, String email, Calendar calendar) throws IOException, MessagingException;

    void sendRemindEmail(String username, String email, Task task);

}
