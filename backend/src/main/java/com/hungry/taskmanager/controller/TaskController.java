package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.dto.CreateTaskDTO;
import com.hungry.taskmanager.dto.EditTaskDTO;
import com.hungry.taskmanager.dto.QueryTaskDTO;
import com.hungry.taskmanager.service.TaskServiceImpl;
import com.hungry.taskmanager.utils.JWTUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin
public class TaskController {
    @Resource
    private TaskServiceImpl taskServiceImpl;
    @ApiOperation(value = "create a task",notes = "前后端测试通过 \n type, taskName, privilege and createDate is required\n father task, members, status,subtask is unnecessary")
    @PostMapping("/addtask")
    @RequiresAuthentication
    public Result<String> addTask(@RequestBody CreateTaskDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
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
    @RequiresAuthentication
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
    @RequiresAuthentication
    public Result<List<Task>> queryTask(@RequestBody QueryTaskDTO filter, HttpServletRequest request){
        Result<List<Task>> result = new Result<>();
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return result.setData(taskServiceImpl.queryTask(filter.setUsername(username))).setCode(200).setMsg("query successfully") ;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<Task>>(500, "server error", null);
        }
    }

    @PostMapping("/edittask")
    @RequiresAuthentication
    @ApiOperation(value = "edit a task",notes = "modified information is required only")
    public Result<String> editTask(@RequestBody EditTaskDTO params) {
        try {
            int result = taskServiceImpl.editTask(params);
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

