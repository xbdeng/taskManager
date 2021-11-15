package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.service.TeamService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
    @ApiOperation(value = "创建小组",notes = "后端已测试通过")
    @RequiresAuthentication
    public Result<String> createTeam(@RequestBody CreateTeamDTO createTeamDTO) {
        teamService.createTeam(createTeamDTO);
        return Result.succ("创建成功");
    }

    @PostMapping("/addmember")
    @RequiresAuthentication
    public Result<String> addMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.addMember(oppoTeamMemberDTO);
        return Result.succ("添加成功");
    }

    @PostMapping("/setadmin")
    public Result<String> setAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.setAdmin(oppoTeamMemberDTO);
        return Result.succ("设置管理员成功");
    }

    @PostMapping("/removemember")
    public Result<String> removeMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.removeMember(oppoTeamMemberDTO);
        return Result.succ("移除成功");
    }

    @PostMapping("/dismiss")
    public Result<String> dismiss(@RequestBody BigInteger teamId) {
        teamService.dismiss(teamId);
        return Result.succ("解散成功");
    }

    @PostMapping("/removeadmin")
    public Result<String> removeAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.removeAdmin(oppoTeamMemberDTO);
        return Result.succ("移除成功");
    }

}
