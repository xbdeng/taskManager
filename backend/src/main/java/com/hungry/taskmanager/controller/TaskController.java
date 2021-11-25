package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.exception.LimitsAuthority;
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

    @ApiOperation(value = "create a task", notes = "前后端测试通过 \n type, taskName, privilege and createDate is required\n father task, members, status,subtask is unnecessary")
    @PostMapping("/addtask")
    @RequiresAuthentication
    public Result<BigInteger> addTask(@RequestBody CreateTaskDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            BigInteger result = taskServiceImpl.addTask(params.setUsername(username));
            return new Result<BigInteger>(200, "successfully add a task", result);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "server error", null);
        }
    }

    @PostMapping("/deletetask")
    @RequiresAuthentication
    public Result<String> deleteTask(@RequestParam("id") long taskId, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            int result = taskServiceImpl.deleteTask(taskId, username);
            if (result != 200) {
                throw new Exception("server error");
            }
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully delete a task", "");
    }

    @PostMapping("/query")
    @RequiresAuthentication
    public Result<List<TaskDTO>> queryTask(@RequestBody QueryTaskDTO filter, HttpServletRequest request) {
        Result<List<TaskDTO>> result = new Result<>();
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return result.setData(taskServiceImpl.queryTask(filter.setUsername(username))).setCode(200).setMsg("query successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<TaskDTO>>(500, "server error", null);
        }
    }


    @PostMapping("/addsubtask")
    @RequiresAuthentication
    @ApiOperation(value = "edit a task", notes = "modified information is required only")
    public Result<String> setSubTask(@RequestBody AddSubTaskDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            taskServiceImpl.addSubTask(params, username);
        } catch (LimitsAuthority e) {
            e.printStackTrace();
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(500, "server error", null);
        }
        return new Result<String>(200, "successfully add a subtask", null);
    }

    @PostMapping("/getmicrosofttask")
    @RequiresAuthentication
    public Result<List<Task>> getMicrosoftTask(@RequestBody String code, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            String code_ = JSONObject.parseObject(code).getString("code");
            List<Task> tasks = MicrosoftUtil.getTasksByCode(code_);
            return new Result<>(200, username, tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/assigntask")
    @RequiresAuthentication
    public Result assignTask(@RequestBody AssignTaskDTO assignTaskDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.assignTask(assignTaskDTO, username);
        } catch (LimitsAuthority e) {
            e.printStackTrace();
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/unassigntask")
    @RequiresAuthentication
    public Result unassignTask(@RequestBody AssignTaskDTO assignTaskDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.unassignTask(assignTaskDTO, username);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/privilege")
    @RequiresAuthentication
    public Result editPrivilege(@RequestBody EditPrivilegeDTO editPrivilegeDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editPrivilege(editPrivilegeDTO, username);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/status")
    @RequiresAuthentication
    public Result editStatus(@RequestBody EditStatusDTO editStatusDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editStatus(editStatusDTO, username);
        } catch (LimitsAuthority e) {
            return Result.fail(202, "你未被分配改任务", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/taskname")
    @RequiresAuthentication
    public Result editTaskName(@RequestBody EditTaskNameDTO editTaskNameDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editTaskName(editTaskNameDTO, username);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/description")
    @RequiresAuthentication
    public Result editDescription(@RequestBody EditTaskDescription editTaskDescription, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editDescription(editTaskDescription, username);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/startdate")
    @RequiresAuthentication
    public Result editTaskStartDate(@RequestBody EditTaskTime editTaskTime, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editStartDate(editTaskTime, username);
        } catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/duedate")
    @RequiresAuthentication
    public Result editTaskDueDate(@RequestBody EditTaskTime editTaskTime,HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editDueDate(editTaskTime,username);
        }catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/reminddate")
    @RequiresAuthentication
    public Result editTaskRemindDate(@RequestBody EditTaskTime editTaskTime,HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editTaskRemindDate(editTaskTime,username);
        }
        catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/location")
    @RequiresAuthentication
    public Result editTaskLocation(@RequestBody EditTaskLocationDTO editTaskLocationDTO,HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.editTaskLocation(editTaskLocationDTO,username);
        }catch (LimitsAuthority e) {
            return Result.fail(201, "权限不足", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/addtag")
    @RequiresAuthentication
    public Result addTag(@RequestBody EditTaskTag editTaskTag, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.addTaskTag(editTaskTag, username);
        }catch (LimitsAuthority e) {
            return Result.fail(202, "你未被分配改任务", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edit/deletetag")
    @RequiresAuthentication
    public Result deleteTag(@RequestBody EditTaskTag editTaskTag, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return taskServiceImpl.deleteTaskTag(editTaskTag, username);
        }catch (LimitsAuthority e) {
            return Result.fail(202, "你未被分配改任务", null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }
}

