package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.List;

public interface TaskService {

    BigInteger addTask(CreateTaskDTO params) throws Exception;

    int deleteTask(@NonNull long taskId) throws Exception;

    List<TaskDTO> queryTask(QueryTaskDTO filter) throws Exception;

//    void editTask(EditTaskDTO params) throws Exception;

    Result assignTask(AssignTaskDTO assignTaskDTO, String username);

    Result unassignTask(AssignTaskDTO assignTaskDTO, String username);

    Result editPrivilege(EditPrivilegeDTO editPrivilegeDTO);


    Result editStatus(EditStatusDTO editStatusDTO);

    Result editTaskName(EditTaskNameDTO editTaskNameDTO);

    Result editDescription(EditTaskDescription editTaskDescription);

    Result editStartDate(EditTaskTime editTaskTime);

    Result editDueDate(EditTaskTime editTaskTime);

    Result editTaskRemindDate(EditTaskTime editTaskTime);

    Result editTaskLocation(EditTaskLocationDTO editTaskLocationDTO);

    Result addTaskTag(EditTaskTag editTaskTag, String username);

    Result deleteTaskTag(EditTaskTag editTaskTag, String username);
}
