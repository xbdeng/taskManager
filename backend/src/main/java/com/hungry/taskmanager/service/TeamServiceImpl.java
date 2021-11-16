package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.TeamMapper;
import com.hungry.taskmanager.dao.TeamUserMapper;
import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.dto.TeamDTO;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.relation_entity.TeamUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    TeamUserMapper teamUserMapper;

    @Override
    public Result createTeam(CreateTeamDTO createTeamDTO) {
        BigInteger creator = userMapper.getIdByName(createTeamDTO.getCreatorName());
        List<User> members = userMapper.selectList(new QueryWrapper<User>().in("username", createTeamDTO.getMembersName()));

        //create team
        Team team = new Team().setTeamName(createTeamDTO.getTeamName()).setCreator(creator).setDescription(createTeamDTO.getDescription());
        teamMapper.insert(team);

        //find team id
        BigInteger teamId = teamMapper.selectOne(new QueryWrapper<Team>().eq("creator", creator).eq("team_name", createTeamDTO.getTeamName())).getTeamId();

        //set creator
        teamUserMapper.insert(new TeamUser().setTeamId(teamId).setUserId(creator).setIdentity("creator"));

        //set members
        for (User member : members) {
            if (!Objects.equals(member.getUsername(), createTeamDTO.getCreatorName())) {
                teamUserMapper.insert(new TeamUser().setTeamId(teamId).setUserId(member.getUserId()).setIdentity("member"));
            }
        }

        return null;
    }

    @Override
    public Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            try {
                teamUserMapper.insert(new TeamUser().setTeamId(oppoTeamMemberDTO.getTeamId()).setUserId(userId).setIdentity("member"));
            } catch (Exception e) {
                e.printStackTrace();//todo 不能在这里处理
            }
        }
        return null;
    }

    @Override
    public Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            teamUserMapper.update(new TeamUser().setTeamId(oppoTeamMemberDTO.getTeamId()).setUserId(userId).setIdentity("admin"), new QueryWrapper<TeamUser>().eq("team_id", oppoTeamMemberDTO.getTeamId()).eq("user_id", userId));
        }
        return null;
    }

    @Override
    public Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        teamUserMapper.delete(new QueryWrapper<TeamUser>().eq("team_id", oppoTeamMemberDTO.getTeamId()).in("user_id", usersId));
        return null;
    }

    @Override
    public Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            UpdateWrapper<TeamUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("team_id", oppoTeamMemberDTO.getTeamId()).eq("user_id", userId).set("identity", "member");
            teamUserMapper.update(null, updateWrapper);
        }
        return null;
    }

    @Override
    public Result dismiss(BigInteger teamId) {
        teamMapper.delete(new QueryWrapper<Team>().eq("team_id",teamId));
        return null;
    }
}
