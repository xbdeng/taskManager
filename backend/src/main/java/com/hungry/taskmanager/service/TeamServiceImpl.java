package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dao.TeamMapper;
import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService{

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public Result createTeam(CreateTeamDTO createTeamDTO) {
        teamMapper.createTeam(createTeamDTO.getTeamName(),createTeamDTO.getDescription(),createTeamDTO.getCreatorName());
        BigInteger teamId = teamMapper.getTeamId(createTeamDTO.getTeamName(),createTeamDTO.getCreatorName());
        BigInteger creatorId = userMapper.getIdByName(createTeamDTO.getCreatorName());
//        List<BigInteger> membersId =
        return null;
    }

    @Override
    public Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public Result dismiss(BigInteger teamId) {
        return null;
    }

    @Override
    public Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }
}
