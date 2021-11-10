package com.hungry.taskmanager;

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
        taskServiceImpl.addTask("ddd", "test", "just for test", tags, 0, 0,
                LocalDateTime.now(), LocalDateTime.now());
        taskServiceImpl.deleteTask(15);

    }

}
