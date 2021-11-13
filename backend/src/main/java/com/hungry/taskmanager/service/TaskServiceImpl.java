package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.relation_entity.UserTaskTag;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        // set state
        Integer status = 0;
        // set father task
        BigInteger fatherTask = null;
        // create new task
        Task task = new Task().setTaskId(id).setCreator(creator).setTaskName(params.getTaskName())
                .setDescription(params.getDescription()).setType(type).setCreateDate(createDate).setDueDate(dueDate)
                .setStatus(status).setFatherTask(fatherTask).setPrivilege(params.getPrivilege());
        // insert task into database
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
        if (id == null){
            return 404;
        }
        try{
            // remove user task tag
            BigInteger utId = userTaskMapper.selectOne(new QueryWrapper<UserTask>().eq("task_id", taskId)).getUtId();
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
    public List<Task> queryTask(@NonNull String username, Integer privilege, String tag, LocalDateTime dueDate) {
        // get userid
        BigInteger userId = userMapper.getIdByName(username);
        QueryWrapper<Task> wrapper = new QueryWrapper<Task>().eq("task.user_id", userId);
        if (privilege != null){
            wrapper = wrapper.eq("privilege", privilege);
        }
        if (tag != null){
            wrapper = wrapper.eq("tag_name", tag);
        }
        if (dueDate != null){
            wrapper = wrapper.ge("dueDate", dueDate);
        }
        List<BigInteger> taskIds = taskMapper.queryTask(wrapper);
        List<Task> tasks = new ArrayList<>();
        for (int  i = 0; i < taskIds.size() ; i++){
            BigInteger id = taskIds.get((int)i);
            Task task = taskMapper.selectById(id);
            // append tags information
            task.setTags(tagMapper.selectTagsByTaskId(id));
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * get task information
     */
    public Task getInfo(long taskId) {
        BigInteger id = BigInteger.valueOf(taskId);
        Task task = taskMapper.selectById(id);
        task.setTags(tagMapper.selectTagsByTaskId(id));
        // append subtask information
//            task.setSubTask(taskMapper.selectList());
        return task;
    }

    /**
     *  modify status of a task
     */
    public int editTask(long id, CreateTaskParams params) throws Exception {
        // get task object
        Task task = getInfo(id);
        UpdateWrapper<Task> wrapper = new UpdateWrapper<Task>().eq("task_id", task.getTaskId());
        // configuration
        if (params.getUsername() != null) {
            BigInteger userId = userMapper.getIdByName(params.getUsername());
            wrapper.set("creator", userId);
        }
        if (params.getTaskName() != null) wrapper.set("task_name", params.getTaskName());
        if (params.getDescription() != null) wrapper.set("description", params.getDescription());
        if (params.getType() != null){
            BigInteger type = assignType(params);
            wrapper.set("type", type);
        }
        if (params.getCreateDate() != null) wrapper.set("create_date", convertGMT(params.getCreateDate()));
        if (params.getDueDate() != null) wrapper.set("due_date", convertGMT(params.getDueDate()));
        if (params.getStatus() != null) wrapper.set("status", params.getStatus());
        if (params.getFatherTask() != null) wrapper.set("father_task", params.getFatherTask());
        if (params.getPrivilege() != null) wrapper.set("privilege", params.getPrivilege());
        if (params.getTags() != null){

        }
        // copy information from original task
        // insert new task record
        // insert father task relationship`
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
                type = team.getGroupId();
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
