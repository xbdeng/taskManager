package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hungry.taskmanager.entity.Task;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.sql.Wrapper;

public interface TaskMapper extends BaseMapper<Task> {
    @Select("SELECT IFNULL(MAX(task_id)+1, 0) FROM task")
    BigInteger newId();

    @Update("UPDATE task SET status = -1 WHERE task_id = #{taskId}")
    int deleteTask(Task entity);
}
