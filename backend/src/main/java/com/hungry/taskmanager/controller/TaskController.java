package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.service.TaskServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Resource
    private TaskServiceImpl taskServiceImpl;

//    @PostMapping("/query")
//    public MyResponse getTasks(@RequestParam("token") String token, @RequestParam("username") String username){
//        return null;
//    }

    @PostMapping("/addtask")
    public MyResponse addTask(@RequestBody CreateTaskParams params){
        try{
            DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
            taskServiceImpl.addTask(params.getUsername(), params.getTitle(), params.getDescription(), params.getTags(),
                    params.getPrivilege(), params.getType(), LocalDateTime.parse(params.getCreateTime(), df),
                    LocalDateTime.parse(params.getDueTime(), df));
        }catch(Exception e){
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return new MyResponse("success");
    }

    @PostMapping("/deletetask")
    public MyResponse deleteTask(@RequestParam("id") long taskId){
        try{
            taskServiceImpl.deleteTask(taskId);
        }catch(Exception e){
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return MyResponse.success();
    }

}
