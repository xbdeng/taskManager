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

    List<Task> queryTask(QueryTaskDTO filter);

}
