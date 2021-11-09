package com.hungry.taskmanager.service;

import com.hungry.taskmanager.entity.Request.CreateTeamInfo;
import com.hungry.taskmanager.entity.Request.OppoTeamMemberInfo;
import com.hungry.taskmanager.entity.Response.MyResponse;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class TeamServiceImpl implements TeamService{
    @Override
    public MyResponse createTeam(CreateTeamInfo createTeamInfo) {
        return null;
    }

    @Override
    public MyResponse addMember(OppoTeamMemberInfo oppoTeamMemberInfo) {
        return null;
    }

    @Override
    public MyResponse setAdmin(OppoTeamMemberInfo oppoTeamMemberInfo) {
        return null;
    }

    @Override
    public MyResponse removeMember(OppoTeamMemberInfo oppoTeamMemberInfo) {
        return null;
    }

    @Override
    public MyResponse dismiss(BigInteger teamId) {
        return null;
    }

    @Override
    public MyResponse removeAdmin(OppoTeamMemberInfo oppoTeamMemberInfo) {
        return null;
    }
}
