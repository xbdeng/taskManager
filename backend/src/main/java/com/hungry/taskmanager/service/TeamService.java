package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.EditTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.dto.TeamDTO;
import com.hungry.taskmanager.entity.Result;

import java.math.BigInteger;

public interface TeamService {
    Result createTeam(CreateTeamDTO createTeamDTO,String creatorName);

    Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result dismiss(BigInteger teamId,String operator);

    Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    int editTeam(EditTeamDTO params);
}
