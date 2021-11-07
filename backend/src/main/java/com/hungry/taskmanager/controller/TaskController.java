package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Resource
    private TaskService taskService;

//    @PostMapping("/query")
//    public MyResponse getTasks(@RequestParam("token") String token, @RequestParam("username") String username){
//        return null;
//    }

}
