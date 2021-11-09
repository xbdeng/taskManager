package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.Request.CreateTeamInfo;
import com.hungry.taskmanager.entity.Request.OppoTeamMemberInfo;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.service.TeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class TeamController {
    @Resource
    TeamService teamService;

    @PostMapping("/createteam")
    public MyResponse createTeam(@RequestBody CreateTeamInfo createTeamInfo) {
        teamService.createTeam(createTeamInfo);
        return new MyResponse("todo");
    }

    @PostMapping("/addmember")
    public MyResponse addMember(@RequestBody OppoTeamMemberInfo oppoTeamMemberInfo) {
        teamService.addMember(oppoTeamMemberInfo);
        return new MyResponse("todo");
    }

    @PostMapping("/setadmin")
    public MyResponse setAdmin(@RequestBody OppoTeamMemberInfo oppoTeamMemberInfo) {
        teamService.setAdmin(oppoTeamMemberInfo);
        return new MyResponse("todo");
    }

    @PostMapping("/removemember")
    public MyResponse removeMember(@RequestBody OppoTeamMemberInfo oppoTeamMemberInfo) {
        teamService.removeMember(oppoTeamMemberInfo);
        return new MyResponse("todo");
    }

    @PostMapping("/dismiss")
    public MyResponse dismiss(@RequestBody BigInteger teamId) {
        teamService.dismiss(teamId);
        return new MyResponse("todo");
    }

    @PostMapping("/removeadmin")
    public MyResponse removeAdmin(@RequestBody OppoTeamMemberInfo oppoTeamMemberInfo) {
        teamService.removeAdmin(oppoTeamMemberInfo);
        return new MyResponse("todo");
    }

}
