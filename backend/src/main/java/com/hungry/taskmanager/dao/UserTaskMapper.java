package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface UserTaskMapper extends BaseMapper<UserTask> {
    @Select("SELECT IFNULL(MAX(ut_id)+1, 0) FROM user_task")
    BigInteger newId();
}
