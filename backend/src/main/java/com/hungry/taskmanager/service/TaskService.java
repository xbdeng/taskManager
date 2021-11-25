package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Task;

import com.hungry.taskmanager.exception.LimitsAuthority;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.List;

public interface TaskService {

    BigInteger addTask(CreateTaskDTO params) throws Exception;

    int deleteTask(@NonNull long taskId,String username) throws Exception;

    List<TaskDTO> queryTask(QueryTaskDTO filter) throws Exception;

//    void editTask(EditTaskDTO params) throws Exception;

    Result assignTask(AssignTaskDTO assignTaskDTO, String username);

    Result unassignTask(AssignTaskDTO assignTaskDTO, String username);

    Result editPrivilege(EditPrivilegeDTO editPrivilegeDTO, String username);


    Result editStatus(EditStatusDTO editStatusDTO,String username);

    Result editTaskName(EditTaskNameDTO editTaskNameDTO,String username);

    Result editDescription(EditTaskDescription editTaskDescription ,String username);

    Result editStartDate(EditTaskTime editTaskTime ,String username);

    Result editDueDate(EditTaskTime editTaskTime ,String username);

    Result editTaskRemindDate(EditTaskTime editTaskTime ,String username);

    Result editTaskLocation(EditTaskLocationDTO editTaskLocationDTO ,String username);

    Result addTaskTag(EditTaskTag editTaskTag, String username );

    Result deleteTaskTag(EditTaskTag editTaskTag, String username);


    void updateDate();

    void addSubTask(AddSubTaskDTO params,String username);

}
