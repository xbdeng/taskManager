package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.relation_entity.TaskTagMap;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagMapper extends BaseMapper<Tag> {
    @Select("SELECT IFNULL(MAX(tag_id)+1, 0) FROM tag")
    BigInteger newId();

    @Insert("INSERT IGNORE  INTO tag(tag_id, tag_name, user_id) VALUES(#{tagId}, #{tagName}, #{userId})")
    int insert(Tag entity);

    @Select("SELECT tag.tag_id AS tag_id, tag.tag_name AS tag_name, tag.user_id AS user_id FROM tag INNER JOIN user_task_tag AS utt ON " +
            "tag.tag_id = utt.tag_id INNER JOIN user_task AS ut ON ut.ut_id = utt.ut_id WHERE ut.user_id = #{userId} and ut.task_id = " +
            "#{taskId}")
    List<Tag>  selectTags(BigInteger taskId, BigInteger userId);

    @Select("SELECT * FROM tag WHERE user_id = #{userId}")
    List<Tag> selectTagsByUser(BigInteger suerId);

    List<TaskTagMap> selectTagsByUserTasks(BigInteger userId, Set<BigInteger> taskIds);
}
