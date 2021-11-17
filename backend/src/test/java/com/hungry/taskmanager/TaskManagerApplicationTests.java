package com.hungry.taskmanager;

import com.hungry.taskmanager.dto.TagDTO;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.QueryTaskParams;
import com.hungry.taskmanager.service.TaskServiceImpl;
import com.hungry.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TaskManagerApplicationTests {
    @Resource
    TaskServiceImpl taskServiceImpl;
    @Resource
    UserService userService;
    @Test
    void contextLoads() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("tom");
        tags.add("jerry");
//        CreateTaskParams params = new CreateTaskParams().setUsername("ddd").setTaskName("try").setDescription("try")
//                .setTags(tags).setPrivilege(1).setType(0).setCreateDate("Sat, 13 Nov 2021 07:33:48 GMT").setDueDate("Tue, 30 Oct 2018 07:33:48 GMT").setStatus(1);
//        int result =taskServiceImpl.editTask(31, params.setFatherTask(BigInteger.valueOf(11)));
//        System.out.println(taskServiceImpl.getInfo(31,3).toString());
//        QueryTaskParams filter = new QueryTaskParams().setUserId(BigInteger.valueOf(3));
//        List<Task> tasks = taskServiceImpl.queryTask(filter);
//        for (Task task: tasks){
//            System.out.println(task.toString());
//        }
        List<Tag> ts = userService.selectTags(new TagDTO().setUsername("ddd"));
        System.out.println(ts.toString());
    }

}
