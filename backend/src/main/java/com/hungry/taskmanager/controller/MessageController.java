package com.hungry.taskmanager.controller;


import com.hungry.taskmanager.entity.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/message")
public class MessageController {
    @RequestMapping("/sendrequest")
    public Result<String> sendRequest(){
        return null;
    }
}
