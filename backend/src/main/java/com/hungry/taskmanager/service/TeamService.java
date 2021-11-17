package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.dto.TeamDTO;
import com.hungry.taskmanager.entity.Result;

import java.math.BigInteger;

public interface TeamService {
    Result createTeam(CreateTeamDTO createTeamDTO,String creatorName);

    Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO);

    Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO);

    Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO);

    Result dismiss(BigInteger teamId);

    Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO);

    boolean isAdmin(BigInteger userId, BigInteger teamId);
}
