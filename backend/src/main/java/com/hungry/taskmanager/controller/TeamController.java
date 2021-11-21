package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.EditTaskDTO;
import com.hungry.taskmanager.dto.EditTeamDTO;
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
    @ApiOperation(value = "创建小组",notes = "√")
    @RequiresAuthentication
    public Result<String> createTeam(@RequestBody CreateTeamDTO createTeamDTO, HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return teamService.createTeam(createTeamDTO, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/addmember")
    @RequiresAuthentication
    @ApiOperation(value = "添加成员",notes = "√")
    public Result addMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO,HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return teamService.addMember(oppoTeamMemberDTO, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/setadmin")
    @ApiOperation(value = "设置管理员",notes = "√")
    @RequiresAuthentication
    public Result setAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO,HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return teamService.setAdmin(oppoTeamMemberDTO, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/removemember")
    @ApiOperation(value = "移除成员",notes = "√")
    @RequiresAuthentication
    public Result removeMember(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO, HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return teamService.removeMember(oppoTeamMemberDTO, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/dismiss")
    @RequiresAuthentication
    @ApiOperation(value = "解散小组",notes = "√")
    public Result dismiss(@RequestBody String teamId,HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            BigInteger teamId_ = JSON.parseObject(teamId).getBigInteger("teamId");
            return teamService.dismiss(teamId_, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/removeadmin")
    @ApiOperation(value = "撤销管理员权限",notes = "√")
    @RequiresAuthentication
    public Result removeAdmin(@RequestBody OppoTeamMemberDTO oppoTeamMemberDTO,HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return teamService.removeAdmin(oppoTeamMemberDTO, username);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(500,"服务器错误",null);
        }
    }

    @PostMapping("/editteam")
    @ApiOperation(value = "修改小组信息",notes = "后端测试通过not yet")
    @RequiresAuthentication
    public Result<String> editTeam(@RequestBody EditTeamDTO params, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        try{
            int result = teamService.editTeam(params);
        }catch(Exception e){
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully edit group", "");
    }


}
