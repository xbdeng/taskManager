package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.relation_entity.UserTaskTag;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface UserTaskTagMapper extends BaseMapper<UserTaskTag> {
    @Select("SELECT IFNULL(MAX(utt_id)+1, 0) FROM user_task_tag")
    BigInteger newId();

}
