package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskFilter;
import com.hungry.taskmanager.service.TaskServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Resource
    private TaskServiceImpl taskServiceImpl;

    @PostMapping("/addtask")
    public Result<String> addTask(@RequestBody CreateTaskParams params) {
        try {
            taskServiceImpl.addTask(params);
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
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully add a task", "");
    }

    @PostMapping("/query")
    public Result<List<Task>> query(@RequestBody QueryTaskFilter params) {
        try {
            DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
            taskServiceImpl.queryTask(params.getUsername(), params.getPrivilege(), params.getTag(),
                    LocalDateTime.parse(params.getDueDate(), df));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @PostMapping("/info")
    public Result<Task> getInfo(long taskId) {
        Task task = null;
        try {
            task = taskServiceImpl.getInfo(taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<Task>(500, "server error", null);
        }
        return new Result<Task>(200, "successfully add a task", task);
    }

    @PostMapping("/edittask")
    public Result<String> editTask(@RequestParam("taskId") long id, @RequestBody CreateTaskParams params) {
        try {
            int result = taskServiceImpl.editTask(id, params);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", null);
        }
        return new Result<String>(200, "successfully add a task", null);
    }
}

