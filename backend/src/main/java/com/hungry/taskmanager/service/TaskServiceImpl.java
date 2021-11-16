package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.post_entities.QueryTaskFilter;
import com.hungry.taskmanager.entity.relation_entity.UserTaskTag;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // generate new taskId
        BigInteger id = taskMapper.newId();
        // get userId by username
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", params.getUsername()));
        BigInteger creator = user.getUserId();
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
        Task task = new Task().setTaskId(id).setCreator(creator).setTaskName(params.getTaskName())
                .setDescription(params.getDescription()).setType(type).setCreateDate(createDate).setDueDate(dueDate)
                .setStatus(status).setFatherTask(fatherTask).setPrivilege(params.getPrivilege());
        // insert task into database
        task.updateDate();
        taskMapper.insert(task);
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
        if (type.equals(BigInteger.valueOf(-1))){
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
    public List<Task> queryTask(QueryTaskFilter filter) {
        List<Task> tasks = taskMapper.queryTask(filter);
        BigInteger userId = filter.getUserId();
        Map<BigInteger, Task> taskMap = new HashMap<>();
        for (Task task : tasks) {
            taskMap.put(task.getTaskId(), task);
            task.setTags(new ArrayList<>());
            QueryTaskFilter subfilter = new QueryTaskFilter().setFatherTask(task.getTaskId()).setUserId(filter.getUserId());
            task.setSubTask(taskMapper.queryTask(subfilter));
            task.updateDate();
        }
        List<Tag> taskTags = tagMapper.selectTagsByUserTasks(userId, taskMap.keySet());
        for (int i = 0 ; i < taskTags.size(); i++){
            taskMap.get(taskTags.get(i).getTaskId()).getTags().add(taskTags.get(i));
        }
        return tasks;
    }

    /**
     * get task information
     */
    public Task getInfo(long taskId, long userId) {
        BigInteger tId = BigInteger.valueOf(taskId);
        BigInteger uId = BigInteger.valueOf(userId);
        Task task = taskMapper.selectById(tId);
        task.setTags(tagMapper.selectTags(tId, uId));
        task.updateDate();
        // append subtask information
        QueryTaskFilter subfilter = new QueryTaskFilter().setFatherTask(task.getTaskId()).setUserId(uId);
        task.setSubTask(taskMapper.selectList(new QueryWrapper<Task>().eq("father_task", tId)));
        return task;
    }

    /**
     *  modify status of a task
     */
    public int editTask(long id, CreateTaskParams params) throws Exception {
        // get task object
        BigInteger taskId = BigInteger.valueOf(id);
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
                type = BigInteger.valueOf(-1);
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
        DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
        return LocalDateTime.parse(date, df);
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
