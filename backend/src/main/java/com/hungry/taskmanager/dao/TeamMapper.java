package com.hungry.taskmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.github.yulichang.base.MPJBaseMapper;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.entity.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TeamMapper extends BaseMapper<Team> {

//    @Insert("INSERT INTO team(team_name, description, creator) values (" +
//            "#{teamName},#{description},#{creator})")
//    void createTeam(String teamName, String description, String creator);
//
//    @Select("SELECT team_id from team WHERE team_name = #{teamName} and creator = #{creator}")
//    BigInteger getTeamId(String teamName, String creator);
//
//    void addMember(BigInteger teamId, List<BigInteger> members);
//
//    void addCreator(BigInteger teamId, BigInteger creator);
//
//    void addAdmin(BigInteger teamId, List<BigInteger> admins);

    List<HashMap<String, Object>> getTeamUserRelationships(List<Team> teams);
}
