package com.hungry.taskmanager.service;

import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.EditTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskParams;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TaskService {

    int addTask(CreateTaskParams params) throws Exception;

    int deleteTask(@NonNull long taskId) throws Exception;

    List<Task> queryTask(QueryTaskParams filter) throws Exception;

    int editTask(EditTaskParams params) throws Exception;

}
