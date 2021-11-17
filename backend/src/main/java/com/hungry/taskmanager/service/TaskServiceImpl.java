package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.EditTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskParams;
import com.hungry.taskmanager.entity.relation_entity.TaskTagMap;
import com.hungry.taskmanager.entity.relation_entity.UserTaskTag;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public int addTask(CreateTaskParams params) throws Exception{
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
        BigInteger id = task.getTaskId();
        // insert tag
        BigInteger tagId = tagMapper.newId();
        Tag tag = new Tag().setUserId(creator);
        List<String> tags = params.getTags();
        if (tags != null && tags.size() > 0){
            for (String s : tags) {
                tag.setTagId(tagId).setTagName(s);
                tagMapper.insert(tag);
                tagId = tagId.add(BigInteger.valueOf(1));
            }
        }
        // insert user task relationship if the type is individual
        if (type.equals(BigInteger.valueOf(0))){
            BigInteger utId = userTaskMapper.newId();
            UserTask ut = new UserTask().setUserId(creator).setTaskId(id).setUtId(utId);
            userTaskMapper.insert(ut);
            // insert user task tag relationship
            return insertUserTaskTag(creator, id, tags);
        }
        return 200;
    }

    /**
     * delete an existing task
     */
    public int deleteTask(@NonNull long taskId) throws Exception {
        BigInteger id = BigInteger.valueOf(taskId);
        try{
            // remove user task tag
            UserTask ut = userTaskMapper.selectOne(new QueryWrapper<UserTask>().eq("task_id", taskId));
            if (ut == null) return 404;
            BigInteger utId = ut.getUtId();
            userTaskTagMapper.delete(new QueryWrapper<UserTaskTag>().eq("ut_id", utId));
            userTaskMapper.delete(new QueryWrapper<UserTask>().eq("ut_id", utId));
            taskMapper.delete(new QueryWrapper<Task>().eq("task_id", id));
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("server error");
        }
        return 200;
    }

    /**
     * query tasks
     */
    public List<Task> queryTask(QueryTaskParams filter) throws Exception {
        BigInteger userId = userMapper.getIdByName(filter.getUsername());
        // configure range
        if (filter.getScheduledTask() == 1){
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
        Map<BigInteger, Task> taskMap = new HashMap<>();
        for (Task task : tasks) {
            taskMap.put(task.getTaskId(), task);
            task.setTags(new ArrayList<>());
            QueryTaskParams subfilter = new QueryTaskParams().setFatherTask(task.getTaskId()).setUserId(filter.getUserId());
            task.setSubTask(taskMapper.queryTask(subfilter));
            task.updateDate();
        }
        List<TaskTagMap> taskTags = tagMapper.selectTagsByUserTasks(userId, taskMap.keySet());
        for (TaskTagMap t: taskTags){
            taskMap.get(t.getTaskId()).getTags().add(t.getTag());
        }
        return tasks;
    }


    /**
     *  modify status of a task
     */
    public int editTask(EditTaskParams params) throws Exception {
        // get task object
        BigInteger taskId = BigInteger.valueOf(params.getTaskId());
        UpdateWrapper<Task> wrapper = new UpdateWrapper<Task>().eq("task_id", taskId);
        // configuration
        if (params.getTaskName() != null) wrapper.set("task_name", params.getTaskName());
        if (params.getDescription() != null) wrapper.set("description", params.getDescription());
        if (params.getType() != null){
            BigInteger type = assignType(params);
            wrapper.set("type", type);
        }
        if (params.getCreateDate() != null) wrapper.set("create_date", convertGMT(params.getCreateDate()));
        if (params.getDueDate() != null) wrapper.set("due_date", convertGMT(params.getDueDate()));
        if (params.getStatus() != null && (params.getStatus() == 0 || params.getStatus() == 1)) wrapper.set("status", params.getStatus());
        if (params.getFatherTask() != null) wrapper.set("father_task", params.getFatherTask());
        if (params.getPrivilege() != null) wrapper.set("privilege", params.getPrivilege());
        if (params.getUsername() != null) {
            BigInteger userId = userMapper.getIdByName(params.getUsername());
            wrapper.set("creator", userId);
        }
        taskMapper.update(null, wrapper);
        return 200;
    }

    private BigInteger assignType(CreateTaskParams params) throws Exception{
        BigInteger type;
        switch(params.getType()){
            case(0):{
                type = BigInteger.valueOf(0);
                break;
            }
            case(1):{
                Team team = teamMapper.selectOne(new QueryWrapper<Team>().eq("team_id", params.getTeamName()));
                type = team.getTeamId();
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

    private int insertUserTaskTag(BigInteger userId, BigInteger taskId, List<String> tagNames){
        // get usertasktag id
        BigInteger uttId = userTaskTagMapper.newId();
        // get usertask id
        UserTask ut = userTaskMapper.selectOne(new QueryWrapper<UserTask>().eq("user_id", userId).eq("task_id", taskId));
        // get tag id
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>().eq("user_id", userId).in("tag_name", tagNames));
        UserTaskTag utt = new UserTaskTag().setUtId(ut.getUtId());
        for (Tag tag : tags){
            utt.setUttId(uttId);
            utt.setTagId(tag.getTagId());
            userTaskTagMapper.insert(utt);
            uttId = uttId.add(BigInteger.valueOf(1));
        }
        return 200;
    }
}
