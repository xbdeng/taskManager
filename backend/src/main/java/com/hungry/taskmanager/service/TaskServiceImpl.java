package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.entity.relation_entity.TaskTag;
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
    private TaskTagMapper taskTagMapper;
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
        BigInteger type = null;
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
        // set date
        DateTimeFormatter df = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalDateTime createDate = LocalDateTime.parse(params.getCreateDate(), df);
        LocalDateTime dueDate = LocalDateTime.parse(params.getDueDate(), df);
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
            for(int i = 0 ; i < tags.size() ; i++){
                tag.setTagId(tagId).setTagName(tags.get(i));
                tagMapper.insert(tag);
                tagId = tagId.add(BigInteger.valueOf(1));
            }
        }
        // insert relationship between tag and task
        TaskTag tt = new TaskTag() .setTaskId(id);
        BigInteger ttId = taskTagMapper.newId();
        List<Tag> addedTags = tagMapper.selectList(new QueryWrapper<Tag>().in("tag_name", tags).eq("user_id", creator));
        for (int i = 0 ; i < addedTags.size(); i++){
            tt.setTtId(ttId).setTagId(addedTags.get(i).getTagId());
            taskTagMapper.insert(tt);
            ttId = ttId.add(BigInteger.valueOf(1));
        }
        // insert user task relationship if the type is individual
        if (type.equals(BigInteger.valueOf(-1))){
            UserTask ut = new UserTask().setUtId(creator).setTaskId(id);
            userTaskMapper.insert(ut);
        }
        return 200;
    }

    /**
     * delete a existing task
     */
    public MyResponse deleteTask(@NonNull long taskId) throws Exception {
        BigInteger id = BigInteger.valueOf(taskId);
        try{
            // remove entity in task_tag
            taskTagMapper.delete(new QueryWrapper<TaskTag>().eq("task_id", id));
            // remove entity in user_tag
            userTaskMapper.delete(new QueryWrapper<UserTask>().eq("task_id", id));
            // disable task in task
            taskMapper.deleteTask(new Task().setTaskId(id));
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("server error");
        }
        return MyResponse.success();
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
            // append subtask information
//            task.setSubTask(taskMapper.selectList());
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
     * @param params
     */
    public void editTask(long id, CreateTaskParams params) {
        // get userId
        BigInteger userId = userMapper.getIdByName(params.getUsername());
        // get task object
        Task task = getInfo(id);
        // copy information from original task
        // insert new task record
        // insert father task relationship`

    }
}
