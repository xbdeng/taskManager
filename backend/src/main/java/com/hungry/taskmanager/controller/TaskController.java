package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskFilter;
import com.hungry.taskmanager.service.TaskServiceImpl;
import com.hungry.taskmanager.utils.JWTUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Resource
    private TaskServiceImpl taskServiceImpl;
    @ApiOperation(value = "create a task",notes = "username, type, taskName, privilege and createDate is required\n father task, members, status,subtask is unnecessary")
    @PostMapping("/addtask")
    @RequiresAuthentication
    public Result<String> addTask(@RequestBody CreateTaskParams params, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        try {
            int result = taskServiceImpl.addTask(params.setUsername(username));
            if (result != 200){
                throw new Exception("server error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully add a task", "");
    }

    @PostMapping("/deletetask")
    public Result<String> deleteTask(@RequestParam("id") long taskId) {
        try {
            int result = taskServiceImpl.deleteTask(taskId);
            if (result != 200){
                throw new Exception("server error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully delete a task", "");
    }

    @PostMapping("/query")
    public Result<List<Task>> query(@RequestBody QueryTaskFilter params) {
        try {
//            DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
//            taskServiceImpl.queryTask(params.getUsername(), params.getPrivilege(), params.getTag(),
//                    LocalDateTime.parse(params.getDueDate(), df));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }



    @PostMapping("/edittask")
    @ApiOperation(value = "create a task",notes = "modified information is required only")
    public Result<String> editTask(@RequestParam("taskId") long id, @RequestBody CreateTaskParams params) {
        try {
            int result = taskServiceImpl.editTask(id, params);
            if (result != 200){
                throw new Exception("server error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", null);
        }
        return new Result<String>(200, "successfully edit a task", null);
    }
}

