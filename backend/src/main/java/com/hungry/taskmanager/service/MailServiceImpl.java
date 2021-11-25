package com.hungry.taskmanager.service;

import com.hungry.taskmanager.utils.RedisUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        message.setSubject("邮箱验证码");
        message.setText("您的验证码: " + code + "\n10分钟内有效");
        redisUtil.set(username + "verifycode",code,10 * 60);
        mailSender.send(message);
    }

//    public void sendSimpleEmail(String email){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("taskmanager@foxmail.com");
//        message.setTo("11913008@mail.sustech.edu.cn");
//        message.setSubject("xyzdltql");
//        message.setText("xyzdl!!!");
//        mailSender.send(message);
//    }

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
