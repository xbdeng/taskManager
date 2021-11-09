package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.relation_entity.TaskTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface TaskTagMapper extends BaseMapper<TaskTag> {
    @Select("SELECT IFNULL(MAX(tt_id)+1, 0) FROM task_tag")
    BigInteger newId();

}
