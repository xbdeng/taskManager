package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.relation_entity.TeamTask;
import com.hungry.taskmanager.entity.relation_entity.TeamUser;
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
import java.util.stream.Collectors;

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
    @Resource
    private TeamTaskMapper teamTaskMapper;
    @Resource
    private TeamUserMapper teamUserMapper;

    /**
     * create a new task and insert insert into database
     */
    public BigInteger addTask(CreateTaskDTO params) throws Exception{
        BigInteger creator = userMapper.getIdByName(params.getUsername());
        // operations according to different types
            // individual task set type column 0
            // team task find id of the team set type id of the team
        if (params.getType() == null){
            params.setType(0);
        }
        BigInteger type = BigInteger.valueOf(params.getType());
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
        if (params.getType() != null && params.getType() == 1){
            if (params.getTeamId() != null){
                TeamTask teamTask = new TeamTask().setTeamId(params.getTeamId()).setTaskId(taskId);
                teamTaskMapper.insert(teamTask);
            }else{
                throw new Exception("params missing");
            }
        }
        // insert tag
        List<String> tags = params.getTags();
        List<Tag> insertedTags = new ArrayList<>();
        if (tags != null && tags.size() > 0){
            for (String s : tags) {
                Tag tag = new Tag().setUserId(creator).setTagName(s);
                tagMapper.insert(tag);
            }
            QueryWrapper<Tag> wrapper = new QueryWrapper<Tag>().eq("user_id", creator).in("tag_name", tags);
            insertedTags = tagMapper.selectList(wrapper);
        }
        // select all related tag ids
        // insert user task relationship if the type is individual
        UserTask ut = new UserTask().setUserId(creator).setTaskId(taskId);
        userTaskMapper.insert(ut);
        // insert user task tag relationship

        for(Tag tag : insertedTags){
            UserTaskTag utt = new UserTaskTag().setUtId(ut.getUtId());
            utt.setTagId(tag.getTagId());
            userTaskTagMapper.insert(utt);
        }
        return taskId;
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
            QueryTaskDTO subfilter = new QueryTaskDTO().setFatherTask(task.getTaskId()).setUserId(filter.getUserId()).setQuerySubTasks(true);
            task.setSubTasks(queryTask(subfilter));
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
    public void editTask(EditTaskDTO params) throws Exception {
        // get userid
        BigInteger userId = userMapper.getIdByName(params.getUsername());
        // get task object
        BigInteger taskId = params.getTaskId();
        UpdateWrapper<Task> wrapper = new UpdateWrapper<Task>().eq("task_id", taskId);
        LocalDateTime createDate = convertGMT(params.getCreateDate());
        LocalDateTime dueDate = convertGMT(params.getDueDate());
        // configuration
        // null check
        if (params.getPrivilege() == null) params.setPrivilege(0);
        wrapper.set("task_name", params.getTaskName()).set("description", params.getDescription()).set("privilege", params.getPrivilege())
                .set("create_date", createDate).set("due_date", dueDate).set("father_task", params.getFatherTask())
                .set("status", params.getStatus());
        //之前的状态 非完成
        int status = taskMapper.selectOne(new QueryWrapper<Task>().eq("task_id", taskId)).getStatus();
        if(status!=1 && params.getStatus()!=null && params.getStatus()==1){ //新完成
            wrapper.set("finish_date",LocalDateTime.now());
        }
        taskMapper.update(null, wrapper);
        // tags
        List<String> tags = params.getTags();
        List<Tag> insertedTags = new ArrayList<>();
        if (tags != null && tags.size() > 0){
            for (String s : tags) {
                Tag tag = new Tag().setUserId(userId).setTagName(s);
                tagMapper.insert(tag);
            }
            QueryWrapper<Tag> tagWrapper = new QueryWrapper<Tag>().eq("user_id", userId).in("tag_name", tags);
            insertedTags = tagMapper.selectList(tagWrapper);
        }
        // get user task tag
        UserTask ut = userTaskMapper.selectOne(new QueryWrapper<UserTask>().eq("user_id", userId).eq("task_id", taskId));
        // get user task tag
        userTaskTagMapper.delete(new QueryWrapper<UserTaskTag>().eq("ut_id", ut.getUtId()));
        for (Tag tag : insertedTags){
            UserTaskTag utt = new UserTaskTag();
            utt.setUtId(ut.getUtId()).setTagId(tag.getTagId());
            userTaskTagMapper.insert(utt);
        }
        // subtasks
        List<String> subtasks = params.getSubTasks();
        if (subtasks != null){
            for (String taskName: subtasks){
                Task newTask = new Task().setTaskName(taskName).setCreator(userId).setType(BigInteger.valueOf(0)).setStatus(0).setPrivilege(0).setFatherTask(taskId);
                taskMapper.insert(newTask);
                UserTask newUT = new UserTask().setUserId(userId).setTaskId(newTask.getTaskId());
                userTaskMapper.insert(newUT);

            }
        }

    }

    @Override
    public Result assignTask(AssignTaskDTO assignTaskDTO, String username) {
        Task task = taskMapper.selectOne(new QueryWrapper<Task>().eq("task_id",assignTaskDTO.getTaskId()));
        if(task.getType().intValue() == 0){
            return Result.fail(201,"个人任务不能分配",null);
        }
        //分配者必须有分配权限 todo

        //被分配者必须在组内
        List<BigInteger> userIds = userMapper.selectList(new QueryWrapper<User>().in("user_id",assignTaskDTO.getUsernames()).select("user_id")).stream().map(User::getUserId).collect(Collectors.toList());
        BigInteger teamId = teamTaskMapper.selectOne(new QueryWrapper<TeamTask>().eq("task_id",assignTaskDTO.getTaskId()).select("team_id")).getTeamId();
        for(BigInteger userId : userIds){
            long c = teamUserMapper.selectCount(new QueryWrapper<TeamUser>().eq("team_id",teamId).eq("user_id",userId));
            if(c==0){
                return Result.fail(202,"该成员不在组内",null);
            }
        }

        for(BigInteger userId:userIds){
            userTaskMapper.insert(new UserTask().setUserId(userId).setTaskId(assignTaskDTO.getTaskId()));
        }
        return Result.succ("分配任务成功");
    }

    public void addSubTask(AddSubTaskDTO params){
        taskMapper.update(new Task(), new UpdateWrapper<Task>().eq("task_id", params.getSubTask()).set("father_task", params.getFatherTask()));
    }


    private LocalDateTime convertGMT(String date){
        if (date == null){
            return null;
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return LocalDateTime.parse(date, f).atZone(ZoneId.from(ZoneOffset.UTC)).withZoneSameInstant(ZoneId.from(ZoneOffset.UTC)).toLocalDateTime();
    }

}
