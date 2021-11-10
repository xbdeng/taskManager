package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.entity.Response.MyResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class TeamServiceImpl implements TeamService{
    @Override
    public MyResponse createTeam(CreateTeamDTO createTeamDTO) {
        return null;
    }

    @Override
    public MyResponse addMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public MyResponse setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public MyResponse removeMember(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }

    @Override
    public MyResponse dismiss(BigInteger teamId) {
        return null;
    }

    @Override
    public MyResponse removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO) {
        return null;
    }
}
