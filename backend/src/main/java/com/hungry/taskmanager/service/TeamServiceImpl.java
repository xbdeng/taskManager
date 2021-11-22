package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.TeamMapper;
import com.hungry.taskmanager.dao.TeamUserMapper;
import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.dto.CreateTeamDTO;
import com.hungry.taskmanager.dto.EditTeamDTO;
import com.hungry.taskmanager.dto.OppoTeamMemberDTO;
import com.hungry.taskmanager.dto.QueryTeamDTO;
import com.hungry.taskmanager.entity.Result;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.relation_entity.TeamUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    TeamUserMapper teamUserMapper;

    @Override
    public Result createTeam(CreateTeamDTO createTeamDTO, String creatorName) {
        BigInteger creator = userMapper.getIdByName(creatorName);

        //create team
        Team team = new Team().setTeamName(createTeamDTO.getTeamName()).setCreator(creator).setDescription(createTeamDTO.getDescription());
        teamMapper.insert(team);

        //find team id
        BigInteger teamId = teamMapper.selectOne(new QueryWrapper<Team>().eq("creator", creator).eq("team_name", createTeamDTO.getTeamName())).getTeamId();

        //set creator
        teamUserMapper.insert(new TeamUser().setTeamId(teamId).setUserId(creator).setIdentity("creator"));

        //set members
        if (createTeamDTO.getMembersName() != null) {
            List<User> members = userMapper.selectList(new QueryWrapper<User>().in("username", createTeamDTO.getMembersName()));
            for (User member : members) {
                if (!Objects.equals(member.getUsername(), creatorName)) {
                    teamUserMapper.insert(new TeamUser().setTeamId(teamId).setUserId(member.getUserId()).setIdentity("member"));
                }
            }
        }

        return Result.succ("创建成功");
    }

    @Override
    public Result addMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator) {
        if(!(isAdmin(operator,oppoTeamMemberDTO.getTeamId()) || isCreator(operator,oppoTeamMemberDTO.getTeamId()))){
            return Result.fail(201,"权限不足",null);
        }
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            try {
                teamUserMapper.insert(new TeamUser().setTeamId(oppoTeamMemberDTO.getTeamId()).setUserId(userId).setIdentity("member"));
            } catch (Exception e) {
                return Result.fail(201,"添加成员失败",null);
            }
        }
        return Result.succ("添加成功");
    }

    @Override
    public Result setAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator) {
        if(!isCreator(operator,oppoTeamMemberDTO.getTeamId())){
            return Result.fail(201,"权限不足",null);
        }
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            teamUserMapper.update(new TeamUser().setTeamId(oppoTeamMemberDTO.getTeamId()).setUserId(userId).setIdentity("admin"), new QueryWrapper<TeamUser>().eq("team_id", oppoTeamMemberDTO.getTeamId()).eq("user_id", userId));
        }
        return Result.succ("设置管理员成功");
    }

    @Override
    public Result removeMember(OppoTeamMemberDTO oppoTeamMemberDTO,String operator) {
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        if(isCreator(operator,oppoTeamMemberDTO.getTeamId())){
            for(BigInteger userId:usersId){
                if(isCreator(userId,oppoTeamMemberDTO.getTeamId())){
                    return Result.fail(201,"群主不能移除自己",null);
                }
            }
        } else if(isAdmin(operator,oppoTeamMemberDTO.getTeamId())){
            for(BigInteger userId:usersId){
                if(isAdmin(userId,oppoTeamMemberDTO.getTeamId()) || isCreator(userId,oppoTeamMemberDTO.getTeamId())){
                    return Result.fail(201,"权限不足",null);
                }
            }
        }else{
            return Result.fail(201,"权限不足,你不是管理员",null);
        }
        teamUserMapper.delete(new QueryWrapper<TeamUser>().eq("team_id", oppoTeamMemberDTO.getTeamId()).in("user_id", usersId));
        return Result.succ("移除成功");
    }

    @Override
    public Result removeAdmin(OppoTeamMemberDTO oppoTeamMemberDTO,String operator) {
        if(!isCreator(operator,oppoTeamMemberDTO.getTeamId())){
            return Result.fail(201,"权限不足",null);
        }
        List<BigInteger> usersId = userMapper.selectList(new QueryWrapper<User>().in("username", oppoTeamMemberDTO.getUserName())).stream().map(User::getUserId).collect(Collectors.toList());
        for (BigInteger userId : usersId) {
            UpdateWrapper<TeamUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("team_id", oppoTeamMemberDTO.getTeamId()).eq("user_id", userId).set("identity", "member");
            teamUserMapper.update(null, updateWrapper);
        }
        return Result.succ("撤销权限成功");
    }

    private boolean isAdmin(String username, BigInteger teamId) {
        BigInteger userId = userMapper.getIdByName(username);
        TeamUser t = teamUserMapper.selectOne(new QueryWrapper<TeamUser>().eq("team_id", teamId).eq("user_id", userId).eq("identity", "admin"));
        return t != null;
    }

    private boolean isAdmin(BigInteger userId, BigInteger teamId){
        TeamUser t = teamUserMapper.selectOne(new QueryWrapper<TeamUser>().eq("team_id", teamId).eq("user_id", userId).eq("identity", "admin"));
        return t != null;
    }

    private boolean isCreator(String username, BigInteger teamId) {
        BigInteger userId = userMapper.getIdByName(username);
        TeamUser t = teamUserMapper.selectOne(new QueryWrapper<TeamUser>().eq("team_id", teamId).eq("user_id", userId).eq("identity","creator"));
        return t != null;
    }

    private boolean isCreator(BigInteger userId, BigInteger teamId){
        TeamUser t = teamUserMapper.selectOne(new QueryWrapper<TeamUser>().eq("team_id", teamId).eq("user_id", userId).eq("identity","creator"));
        return t != null;
    }

    @Override
    public Result dismiss(BigInteger teamId,String operator) {
        if(!isCreator(operator,teamId)){
            return Result.fail(201,"只有群主能解散群",null);
        }
        teamMapper.delete(new QueryWrapper<Team>().eq("team_id", teamId));
        return Result.succ("解散成功");
    }

    @Override
    public Result editTeam(EditTeamDTO params,String operator){
        if(!(isCreator(operator,params.getTeamId())||isAdmin(operator,params.getTeamId()))){
            return Result.fail(201,"权限不足",null);
        }
        Team team = new Team();
        UpdateWrapper<Team> wrapper = new UpdateWrapper<Team>().eq("team_id", params.getTeamId()) ;
        if (params.getTeamName() != null){
            wrapper = wrapper.set("team_name", params.getTeamName());
        }
        if (params.getDescription() != null){
            wrapper = wrapper.set("description", params.getDescription());
        }
        teamMapper.update(team, wrapper);
        return Result.succ("修改成功");
    }

    @Override
    public boolean isInTeam(BigInteger userId, BigInteger teamId){
        Long count = teamUserMapper.selectCount(new QueryWrapper<TeamUser>().eq("team_id", teamId).eq("user_id", userId));
        if (count > 0 ){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Result<List<QueryTeamDTO>> queryTeam(String teamName, BigInteger teamId) {
        if(teamId!=null){
            Team team = teamMapper.selectOne(new QueryWrapper<Team>().eq("team_id",teamId));
            QueryTeamDTO queryTeamDTO = new QueryTeamDTO(team.getTeamId(),team.getTeamName(),team.getDescription(),
                    userMapper.selectById(team.getCreator()).getUsername());
            List<QueryTeamDTO> re = new ArrayList<>(1);
            re.add(queryTeamDTO);
            return new Result<>(200,"查询成功",re);
        }
        List<Team> teams = teamMapper.selectList(new QueryWrapper<Team>().like("team_name",teamName));
        List<QueryTeamDTO> queryTeamDTOS = new ArrayList<>();
        for(Team team:teams){
            QueryTeamDTO queryTeamDTO = new QueryTeamDTO(team.getTeamId(),team.getTeamName(),team.getDescription(),
                    userMapper.selectById(team.getCreator()).getUsername());
            queryTeamDTOS.add(queryTeamDTO);
        }
        return new Result<>(200,"查询成功",queryTeamDTOS);
    }
}
