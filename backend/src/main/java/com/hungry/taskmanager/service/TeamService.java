package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Result;

import java.math.BigInteger;
import java.util.List;

public interface TeamService {
    Result createTeam(CreateTeamDTO createTeamDTO,String creatorName);

    Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result dismiss(BigInteger teamId,String operator);

    Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator);

    Result editTeam(EditTeamDTO params,String operator);

    boolean isInTeam(BigInteger userId, BigInteger teamId);

    Result<List<QueryTeamDTO>> queryTeam(String teamName, BigInteger teamId);

    Result withdraw(BigInteger teamId, String username);

}
