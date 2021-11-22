package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.github.yulichang.base.MPJBaseMapper;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TeamMapper extends BaseMapper<Team> {

    List<HashMap<String, Object>> getTeamUserRelationships(List<Team> teams);

    List<User> getTeamCreatorAndAdministrators(BigInteger teamId);

    @Select("SELECT team_name FROM team WHERE team_id = #{teamId}")
    String getTeamNameById(BigInteger teamId);
}
