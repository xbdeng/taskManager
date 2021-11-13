package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskFilter;
import com.hungry.taskmanager.service.TaskServiceImpl;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "create a task",notes = "username, type, taskName, privilege and createDate is required\n father task, members, status is unnecessary")
    @PostMapping("/addtask")
    public Result<String> addTask(@RequestBody CreateTaskParams params) {
        try {
            int result = taskServiceImpl.addTask(params);
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

//    @PostMapping("/query")
//    public Result<List<Task>> query(@RequestBody QueryTaskFilter params) {
//        try {
//            DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
//            taskServiceImpl.queryTask(params.getUsername(), params.getPrivilege(), params.getTag(),
//                    LocalDateTime.parse(params.getDueDate(), df));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }

    @PostMapping("/info")
    public Result<Task> getInfo(long taskId, long userId) {
        Task task = null;
        try {
            task = taskServiceImpl.getInfo(taskId, userId);
            if (task == null){
                throw new Exception("server error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<Task>(500, "server error", null);
        }
        return new Result<Task>(200, "successfully get a task", task);
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

