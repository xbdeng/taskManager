package com.hungry.taskmanager;

import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.post_entities.CreateTaskParams;
import com.hungry.taskmanager.service.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TaskManagerApplicationTests {
    @Resource
    TaskServiceImpl taskServiceImpl;
    @Test
    void contextLoads() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("tom");
        tags.add("jerry");
        CreateTaskParams params = new CreateTaskParams().setUsername("ddd").setTaskName("testcase").setDescription("try")
                .setTags(tags).setPrivilege(1).setType(0).setCreateDate("Tue, 30 Oct 2018 07:33:48 GMT").setDueDate("Tue, 30 Oct 2018 07:33:48 GMT");
        int result =taskServiceImpl.addTask(params);


    }

}
