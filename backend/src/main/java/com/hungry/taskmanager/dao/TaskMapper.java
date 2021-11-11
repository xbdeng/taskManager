package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hungry.taskmanager.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

public interface TaskMapper extends BaseMapper<Task> {
    @Select("SELECT IFNULL(MAX(task_id)+1, 0) FROM task")
    BigInteger newId();

    @Update("UPDATE task SET status = -1 WHERE task_id = #{taskId}")
    void deleteTask(Task entity);

    @Select("SELECT task.task_id FROM task INNER JOIN task_tag ON task.task_id = task_tag.task_id INNER JOIN tag " +
            "ON task_tag.tag_id = tag.tag_id")
    List<BigInteger> queryTask(@Param(Constants.WRAPPER) Wrapper<Task> queryWrapper);


}
