package com.hungry.taskmanager;

import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.service.TaskServiceImpl;
import com.hungry.taskmanager.service.TeamService;
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
    @Resource
    TeamService teamService;
    @Test
    void contextLoads() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("tom");
        tags.add("jerry");
//        CreateTaskParams params = new CreateTaskParams().setUsername("ddd").setTaskName("try").setDescription("try")
//                .setTags(tags).setPrivilege(1).setType(0).setCreateDate("2020-11-16T00:01:45.000Z").setDueDate("2020-11-15T00:01:45.000Z");
//        int result =taskServiceImpl.addTask(params);
//        QueryTaskDTO filter = new QueryTaskDTO().setUsername("ddd");
//        List<Task> tasks = taskServiceImpl.queryTask(filter);
//        for (Task task: tasks){
//            System.out.println(task.toString());
//        }
//        List<Tag> ts = userService.selectTags(new TagDTO().setUsername("ddd"));
//        System.out.println(ts.toString());
//        EditTeamDTO dto = new EditTeamDTO().setTeamId(BigInteger.valueOf(9)).setDescription("description");
        userService.editUser(new EditUserDTO().setEmail("ComLqWork@outlook.com").setUsername("nametest").setPassword("password").setFirstName("thomas").setPhone("131111111"));
//        teamService.editTeam(dto);
        List<TeamDTO> result = userService.getTeams("xyzdl");
        for(TeamDTO t: result){
            System.out.println(t.toString());
        }
        System.out.println();
    }

}
