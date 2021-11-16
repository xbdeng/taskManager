package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.service.TeamService;
import com.hungry.taskmanager.utils.JWTUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public Result<String> createTeam(@RequestBody CreateTeamDTO createTeamDTO, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        teamService.createTeam(createTeamDTO,username);
        return Result.succ("创建成功");
    }

    @PostMapping("/addmember")
    @RequiresAuthentication
    @ApiOperation(value = "添加成员",notes = "后端已测试通过")
    public Result<String> addMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.addMember(oppoTeamMemberDTO);
        return Result.succ("添加成功");
    }

    @PostMapping("/setadmin")
    @ApiOperation(value = "设置管理员",notes = "后端已测试通过")
    @RequiresAuthentication
    public Result<String> setAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.setAdmin(oppoTeamMemberDTO);
        return Result.succ("设置管理员成功");
    }

    @PostMapping("/removemember")
    @ApiOperation(value = "移除成员",notes = "后端已测试通过")
    public Result<String> removeMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.removeMember(oppoTeamMemberDTO);
        return Result.succ("移除成功");
    }

    @PostMapping("/dismiss")
    @ApiOperation(value = "解散小组",notes = "后端已测试通过")
    public Result<String> dismiss(@RequestBody String teamId) {
        BigInteger teamId_ = JSON.parseObject(teamId).getBigInteger("teamId");
        teamService.dismiss(teamId_);
        return Result.succ("解散成功");
    }

    @PostMapping("/removeadmin")
    @ApiOperation(value = "撤销管理员权限",notes = "后端已测试通过")
    public Result<String> removeAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO) {
        teamService.removeAdmin(oppoTeamMemberDTO);
        return Result.succ("移除成功");
    }

}
