package com.hungry.taskmanager;

import com.hungry.taskmanager.entity.Task;
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
//        List<Task> tasks = taskServiceImpl.queryTask("ddd", 0, "jerry",LocalDateTime.now());
//        for(Task task : tasks){
//            System.out.println(task.toString());
//        }
        Task task =taskServiceImpl.getInfo(19);
        System.out.println(task.toString());


    }

}
