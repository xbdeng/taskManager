package com.hungry.taskmanager.component;


import com.hungry.taskmanager.service.TaskService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    @Resource
    private TaskService taskService;

    @Scheduled(cron="0/59 * * * * ? ")
    public void updateDate(){
        taskService.updateDate();
    }

}
