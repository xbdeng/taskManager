package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.dto.CreateTaskDTO;
import com.hungry.taskmanager.dto.EditTaskDTO;
import com.hungry.taskmanager.dto.QueryTaskDTO;
import com.hungry.taskmanager.entity.relation_entity.UserTaskTag;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService{
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private UserTaskTagMapper userTaskTagMapper;
    @Resource
    private UserTaskMapper userTaskMapper;
    @Resource
    private TeamMapper teamMapper;

    /**
     * create a new task and insert insert into database
     */
    public int addTask(CreateTaskDTO params) throws Exception{
        BigInteger creator = userMapper.getIdByName(params.getUsername());
        // operations according to different types
            // individual task set type column -1
            // team task find id of the team set type id of the team
        BigInteger type = assignType(params);
        // set date
        LocalDateTime createDate = convertGMT(params.getCreateDate());
        LocalDateTime dueDate = convertGMT(params.getDueDate());
        // set status
        Integer status = 0;
        // set father task
        BigInteger fatherTask = null;
        // create new task
        Task task = new Task().setCreator(creator).setTaskName(params.getTaskName())
                .setDescription(params.getDescription()).setType(type).setCreateDate(createDate).setDueDate(dueDate)
                .setStatus(status).setFatherTask(fatherTask).setPrivilege(params.getPrivilege());
        // insert task into database
        task.updateDate();
        taskMapper.insert(task);
        BigInteger taskId = task.getTaskId();
        // insert tag
        List<String> tags = params.getTags();

        if (tags != null && tags.size() > 0){
            for (String s : tags) {
                Tag tag = new Tag().setUserId(creator).setTagName(s);
                tagMapper.insert(tag);
            }
        }
        // select all related tag ids
        QueryWrapper<Tag> wrapper = new QueryWrapper<Tag>().eq("user_id", creator).in("tag_name", tags);
        List<Tag> insertedTags = tagMapper.selectList(wrapper);
        // insert user task relationship if the type is individual
        if (type.equals(BigInteger.valueOf(0))){
            UserTask ut = new UserTask().setUserId(creator).setTaskId(taskId);
            userTaskMapper.insert(ut);
            // insert user task tag relationship

            for(Tag tag : insertedTags){
                UserTaskTag utt = new UserTaskTag().setUtId(ut.getUtId());
                utt.setTagId(tag.getTagId());
                userTaskTagMapper.insert(utt);
            }
        }
        return 200;
    }

    /**
     * delete an existing task
     */
    public int deleteTask(@NonNull long taskId) throws Exception {
        BigInteger id = BigInteger.valueOf(taskId);
        UserTask ut = userTaskMapper.selectOne(new QueryWrapper<UserTask>().eq("task_id", taskId));
        BigInteger utId = ut.getUtId();
        userTaskTagMapper.delete(new QueryWrapper<UserTaskTag>().eq("ut_id", utId));
        userTaskMapper.delete(new QueryWrapper<UserTask>().eq("ut_id", utId));
        taskMapper.delete(new QueryWrapper<Task>().eq("task_id", id));
        return 200;
    }

    /**
     * query tasks
     */
    public List<Task> queryTask(QueryTaskDTO filter) throws Exception {
        BigInteger userId = userMapper.getIdByName(filter.getUsername());
        filter.setUserId(userId);
        // configure range
        if (filter.getScheduledTask() != null && filter.getScheduledTask() == 1){
            switch(filter.getTimeRange()){
                case(0):{
                    LocalDateTime currentDate = convertGMT(filter.getCurrentDate());
                    LocalDateTime requiredDate = LocalDateTime.of(currentDate.toLocalDate(), LocalTime.MIN);
                    filter.setRequiredDate(requiredDate);
                    break;
                }
                case(1):{
                    LocalDateTime currentDate = convertGMT(filter.getCurrentDate());
                    LocalDateTime requiredDate = LocalDateTime.of(currentDate.toLocalDate().plus(6,ChronoUnit.DAYS), LocalTime.MIN);
                    filter.setRequiredDate(requiredDate);
                    break;
                }
                case(2):{
                    LocalDateTime currentDate = convertGMT(filter.getCurrentDate());
                    LocalDateTime requiredDate = LocalDateTime.of(currentDate.toLocalDate().plus(7,ChronoUnit.DAYS), LocalTime.MIN);
                    filter.setRequiredDate(requiredDate);
                    break;
                }
                default:{
                    throw new Exception("timerange type error");
                }
            }
        }
        List<Task> tasks = taskMapper.queryTask(filter.setUserId(userId));
        if (tasks.size() == 0){
            return new ArrayList<>();
        }
        Map<BigInteger, Task> taskMap = new HashMap<>();
        for (Task task : tasks) {
            taskMap.put(task.getTaskId(), task);
            task.setTags(new ArrayList<>());
            QueryTaskDTO subfilter = new QueryTaskDTO().setFatherTask(task.getTaskId()).setUserId(filter.getUserId());
            task.setSubTask(taskMapper.queryTask(subfilter));
            task.updateDate();
        }
        List<HashMap<String, Object>> taskTags = tagMapper.selectTagsByUserTasks(userId, taskMap.keySet());
        for (HashMap<String, Object> map: taskTags){
            BigInteger taskId  = BigInteger.valueOf((Long)map.get("task_id"));
            String tagName = ((String)map.get("tag_name"));
            taskMap.get(taskId).getTags().add(tagName);
        }
        return tasks;
    }


    /**
     *  modify status of a task
     */
    public int editTask(EditTaskDTO params) throws Exception {
        // get task object
        BigInteger taskId = BigInteger.valueOf(params.getTaskId());
        UpdateWrapper<Task> wrapper = new UpdateWrapper<Task>().eq("task_id", taskId);
        // configuration
        if (params.getTaskName() != null) wrapper.set("task_name", params.getTaskName());
        if (params.getDescription() != null) wrapper.set("description", params.getDescription());
        if (params.getCreateDate() != null) wrapper.set("create_date", convertGMT(params.getCreateDate()));
        if (params.getDueDate() != null) wrapper.set("due_date", convertGMT(params.getDueDate()));
        if (params.getStatus() != null && (params.getStatus() == 0 || params.getStatus() == 1)) wrapper.set("status", params.getStatus());
        if (params.getFatherTask() != null) wrapper.set("father_task", params.getFatherTask());
        if (params.getPrivilege() != null) wrapper.set("privilege", params.getPrivilege());
        taskMapper.update(null, wrapper);
        return 200;
    }

    private BigInteger assignType(CreateTaskDTO params) throws Exception{
        BigInteger type;
        switch(params.getType()){
            case(0):{
                type = BigInteger.valueOf(0);
                break;
            }
            case(1):{
                // Todo 插入team task 表 插进去 而且只能用在createtask
                type = BigInteger.valueOf(1);
                break;
            }
            default:{
                throw new Exception();
            }
        }
        return type;
    }


    private LocalDateTime convertGMT(String date){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return LocalDateTime.parse(date, f).atZone(ZoneId.from(ZoneOffset.UTC)).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

}
