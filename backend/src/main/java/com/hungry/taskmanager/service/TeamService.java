package com.hungry.taskmanager.service;

import com.hungry.taskmanager.entity.Request.CreateTeamInfo;
import com.hungry.taskmanager.entity.Request.OppoTeamMemberInfo;
import com.hungry.taskmanager.entity.Response.MyResponse;

import java.math.BigInteger;

public interface TeamService {
    MyResponse createTeam(CreateTeamInfo createTeamInfo);

    MyResponse addMember(OppoTeamMemberInfo oppoTeamMemberInfo);

    MyResponse setAdmin(OppoTeamMemberInfo oppoTeamMemberInfo);

    MyResponse removeMember(OppoTeamMemberInfo oppoTeamMemberInfo);

    MyResponse dismiss(BigInteger teamId);

    MyResponse removeAdmin(OppoTeamMemberInfo oppoTeamMemberInfo);
}
