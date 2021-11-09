package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface TagMapper extends BaseMapper<Tag> {
    @Select("SELECT IFNULL(MAX(tag_id)+1, 0) FROM tag")
    BigInteger newId();

    @Insert("INSERT IGNORE INTO tag(tag_id, tag_name, user_id) VALUES(#{tagId}, #{tagName}, #{userId})")
    int insert(Tag entity);
}
