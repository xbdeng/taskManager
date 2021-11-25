package com.hungry.taskmanager.service;

import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.utils.RedisUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;


@Service
public class MailServiceImpl implements MailService{

    @Resource
    public JavaMailSender mailSender;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void sendVerifyEmail(String email,String username) {
        String code = getRandomNumCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("taskmanager@foxmail.com");
        message.setTo(email);
        message.setSubject("【taskmanager】邮箱验证码");
        message.setText("您的验证码: " + code + "\n10分钟内有效\nhungry团队");
        redisUtil.set(username + "verifycode",code,10 * 60);
        mailSender.send(message);
    }

    @Override
    public void sendRemindEmail(String username, String email, Task task) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("taskmanager@foxmail.com");
        message.setTo(email);
        message.setSubject("【taskmanager】任务提醒");
        message.setText("task name: " +task.getTaskName()+
                "\ntask description: "+task.getDescription()+
                "\ntask location" + task.getLocation()+
                "\n\n\nhungry团队");
        LocalDateTime localDateTime = task.getRemindDate();
        Date date = new Date(localDateTime.getYear(),localDateTime.getMonthValue()-1,localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());
        message.setSentDate(date);
        mailSender.send(message);
    }

    @Override
    public void sendCalendar(String username, String email, Calendar calendar) throws IOException, MessagingException {
        FileOutputStream fout = new FileOutputStream(username + "_calendar.ics");
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar,fout);
        fout.close();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("taskmanager@foxmail.com");
        messageHelper.setTo(email);
        messageHelper.setSubject("【taskmanager】用户任务数据导出");
        messageHelper.setText("亲爱的" + username +":\n\t您的任务数据已经通过邮件附件发送，感谢您选择hungry团队的产品！");
        messageHelper.addAttachment(username + "_calendar.ics",new File(username + "_calendar.ics"));
        mailSender.send(mimeMessage);

        File file = new File(username + "_calendar.ics");
        file.delete();
    }



    private static String getRandomNumCode(){
        StringBuilder codeNum = new StringBuilder();
        int [] numbers = {0,1,2,3,4,5,6,7,8,9};
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int next = random.nextInt(10000);//目的是产生足够随机的数，避免产生的数字重复率高的问题
            codeNum.append(numbers[next % 10]);
        }
        return codeNum.toString();
    }

}
