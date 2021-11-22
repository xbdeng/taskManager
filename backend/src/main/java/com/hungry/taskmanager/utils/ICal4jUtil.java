package com.hungry.taskmanager.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.dao.UserTaskMapper;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.FixedUidGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ICal4jUtil {

    private static UserTaskMapper userTaskMapper;

    private static UserMapper userMapper;

    @Resource
    public void setUserTaskMapper(UserTaskMapper userTaskMapper) {
        ICal4jUtil.userTaskMapper = userTaskMapper;
    }

    @Resource
    public void setUserMapper(UserMapper userMapper) {
        ICal4jUtil.userMapper = userMapper;
    }


    public static VEvent TaskToVEvent(Task task) throws SocketException {
        String title = task.getTaskName();
        String description = task.getDescription();
        Calendar startDate = new GregorianCalendar();
        startDate.set(task.getCreateDate().getYear(), task.getCreateDate().getMonthValue(),
                task.getCreateDate().getDayOfMonth(), task.getCreateDate().getHour(), task.getCreateDate().getMinute(),
                task.getCreateDate().getSecond());

        Calendar endDate = new GregorianCalendar();
        endDate.set(task.getDueDate().getYear(), task.getDueDate().getMonthValue(), task.getDueDate().getDayOfMonth(),
                task.getDueDate().getHour(), task.getDueDate().getMinute(), task.getDueDate().getSecond());


        DateTime startTime = new DateTime(startDate.getTime());
        DateTime endTime = new DateTime(endDate.getTime());
        VEvent vEvent = new VEvent(startTime, endTime, title);

        Uid uid = new FixedUidGenerator("aba").generateUid();
        vEvent.getProperties().add(uid);

        Description eventDescription = new Description();
        eventDescription.setValue(description);
        vEvent.getProperties().add(eventDescription);

        Location location = new Location();
        location.setValue(task.getLocation());
        vEvent.getProperties().add(location);

        //添加协作者
        //获取队伍信息
        List<BigInteger> usersId = userTaskMapper.selectList(new QueryWrapper<UserTask>().in("task_id", task.getTaskId())).stream().map(UserTask::getUserId).collect(Collectors.toList());
        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("user_id", usersId));

        for (User user : users) {
            Attendee attendee = new Attendee(URI.create("mailto:" + user.getEmail()));
            attendee.getParameters().add(Role.REQ_PARTICIPANT);
            attendee.getParameters().add(new Cn(user.getUsername()));
            vEvent.getProperties().add(attendee);
        }

        return vEvent;
    }


    public static net.fortuna.ical4j.model.Calendar TasksToCalendar(ArrayList<Task> tasks) throws SocketException {
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(new ProdId("-//SUSTech//taskmanager 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        for (Task task : tasks) {
            calendar.getComponents().add(TaskToVEvent(task));
        }
        return calendar;
    }

}
