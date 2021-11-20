package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.dto.QueryTaskDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface TaskMapper extends BaseMapper<Task> {

    @Select("SELECT task.task_id FROM task INNER JOIN task_tag ON task.task_id = task_tag.task_id INNER JOIN tag " +
            "ON task_tag.tag_id = tag.tag_id")
    List<BigInteger> queryTask(@Param(Constants.WRAPPER) Wrapper<Task> queryWrapper);

    List<Task> queryTask(QueryTaskDTO filter);

    Object selectTagsForTasks(BigInteger userId);

}
