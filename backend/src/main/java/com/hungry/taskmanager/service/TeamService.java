package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.entity.Response.MyResponse;

import java.math.BigInteger;

public interface TeamService {
    MyResponse createTeam(CreateTeamDTO createTeamDTO);

    MyResponse addMember(OppoTeamMemberDTO oppoTeamMemberDTO);

    MyResponse setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO);

    MyResponse removeMember(OppoTeamMemberDTO oppoTeamMemberDTO);

    MyResponse dismiss(BigInteger teamId);

    MyResponse removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO);
}
