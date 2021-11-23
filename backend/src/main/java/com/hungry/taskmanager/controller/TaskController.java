package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.dto.AddSubTaskDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.dto.CreateTaskDTO;
import com.hungry.taskmanager.dto.EditTaskDTO;
import com.hungry.taskmanager.dto.QueryTaskDTO;
import com.hungry.taskmanager.service.TaskServiceImpl;
import com.hungry.taskmanager.utils.JWTUtil;
import com.hungry.taskmanager.utils.MicrosoftUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
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
    public Result<BigInteger> addTask(@RequestBody CreateTaskDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            BigInteger result = taskServiceImpl.addTask(params.setUsername(username));
            return new Result<BigInteger>(200, "successfully add a task", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "server error", null);
        }

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
            return result.setData(taskServiceImpl.queryTask(filter.setUsername(username).setOnlyTopTask(true))).setCode(200).setMsg("query successfully") ;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<Task>>(500, "server error", null);
        }
    }

    @PostMapping("/edittask")
    @RequiresAuthentication
    @ApiOperation(value = "edit a task",notes = "modified information is required only")
    public Result<String> editTask(@RequestBody EditTaskDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            taskServiceImpl.editTask(params.setUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", null);
        }
        return new Result<String>(200, "successfully edit a task", null);
    }

    @PostMapping("/addsubtask")
    @RequiresAuthentication
    @ApiOperation(value = "edit a task",notes = "modified information is required only")
    public Result<String> setSubTask(@RequestBody AddSubTaskDTO params) {
        try {
            taskServiceImpl.addSubTask(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", null);
        }
        return new Result<String>(200, "successfully add a subtask", null);
    }

    @PostMapping("/getmicrosofttask")
    @RequiresAuthentication
    public Result<List<Task>> getMicrosoftTask(@RequestBody String code) {
        try {
            String code_ = JSONObject.parseObject(code).getString("code");
            List<Task> tasks = MicrosoftUtil.getTasksByCode(code_);
            return new Result<>(200, "请求成功", tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }
}

