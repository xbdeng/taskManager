package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskFilter;
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
    public Result<String> addTask(@RequestBody CreateTaskParams params){
        try{
            taskServiceImpl.addTask(params);
        }catch(Exception e){
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully add a task", "");
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

    @PostMapping("/query")
    public MyResponse query(@RequestBody QueryTaskFilter params){
        try{
            DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
            taskServiceImpl.queryTask(params.getUsername(), params.getPrivilege(),params.getTag(),
                    LocalDateTime.parse(params.getDueDate(),df));
        }catch(Exception e){
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return new MyResponse("success");
    }

    @PostMapping("/info")
    public MyResponse getInfo(long taskId){
        try{
            Task task = taskServiceImpl.getInfo(taskId);
        }catch(Exception e){
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return new MyResponse("success");
    }

    @PostMapping("/edittask")
    public MyResponse editTask(@RequestParam("taskId") long id, @RequestBody CreateTaskParams params){
        try{
            taskServiceImpl.editTask(id, params);
        }catch(Exception e){
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return new MyResponse("success");
    }
}
