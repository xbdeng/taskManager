package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.relation_entity.TaskTag;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserDAO userDAO;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private TaskTagMapper taskTagMapper;
    @Resource
    private UserTaskMapper userTaskMapper;

    /**
     * create a new task and insert insert into database
     */
    public MyResponse addTask(String username, String title, String description, List<String> tags, Integer privilege,
                              Integer type, LocalDateTime createDate, LocalDateTime dueDate){
        // 1. get id for new task
        BigInteger id = taskMapper.newId();
        try {
            // 2. access userid using username
            User user = userDAO.selectOne(new QueryWrapper<User>().eq("username", username));
            if (user == null) {
                throw new Exception("could not find user");
            }
            BigInteger userId = user.getUserId();
            Task task = new Task();
            task.setTaskId(id).setUserId(userId).setTitle(title).setDescription(description).setCreateDate(createDate).
                    setDueDate(dueDate).setStatus(0).setPrivilege(privilege).setType(type);
            // 3. insert task record into database
            taskMapper.insert(task);
            // 4. insert tag
            BigInteger tagId = tagMapper.newId();
            Tag tag = new Tag().setUserId(userId);
            if (tags!=null && tags.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    tag.setTagId(tagId).setTagName(tags.get(i));
                    tagMapper.insert(tag);
                    tagId = tagId.add(BigInteger.valueOf(1));
                }
            }
            // 5. insert relationship between tag and task
            TaskTag tt = new TaskTag().setTaskId(id);
            BigInteger ttId = taskTagMapper.newId();
            List<Tag> addedTags = tagMapper.selectList(new QueryWrapper<Tag>().in("tag_name", tags).eq("user_id", userId));
            for (int i = 0 ; i < addedTags.size() ; i++){
                tt.setTtId(ttId).setTagId(addedTags.get(i).getTagId());
                taskTagMapper.insert(tt);
                ttId = ttId.add(BigInteger.valueOf(1));
            }
            // 6. insert relationship between user and task
            UserTask ut = new UserTask().setUtId(userId).setTaskId(id);
            userTaskMapper.insert(ut);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new MyResponse("server error");
        }
        return MyResponse.success();
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
}
