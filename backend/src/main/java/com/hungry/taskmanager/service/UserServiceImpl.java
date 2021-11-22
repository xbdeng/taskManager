package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.relation_entity.Contact;
import com.hungry.taskmanager.entity.relation_entity.TeamUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {


    //todo 用@Autowired爆红
    @Resource
    private UserMapper userMapper;

    @Resource
    private ContactMapper contactMapper;

    @Resource
    private TeamUserMapper teamUserMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private TaskService taskService;

    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userMapper.findPermsByRoleId(id);
    }

    @Override
    public User getUserByPass(String username, String password) {
        return userMapper.getUserByPass(username, password);
    }

    @Override
    public List<UserDTO> getAddressBook(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        List<BigInteger> friendsId = contactMapper.selectList(new QueryWrapper<Contact>().eq("person", userId)).stream().map(Contact::getFriend).collect(Collectors.toList());
        if(friendsId.isEmpty()){
            return new ArrayList<UserDTO>();
        }
        return userMapper.selectList(new QueryWrapper<User>().in("user_id", friendsId)).stream().map(User::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<TeamDTO> getTeams(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        //获取全部的teamsId
        List<BigInteger> teamsId = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", userId)).stream().map(TeamUser::getTeamId).collect(Collectors.toList());
        //获取全部的teams
        try{
            return getTeamDTOsByTeamIds(teamsId);//todo
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<TeamDTO> getAdminTeams(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        //获取管理的的teamsId
        List<String> t = new ArrayList<>(2);
        t.add("admin");
        t.add("creator");
        List<BigInteger> teamsId = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", userId).in("identity", t)).stream().map(TeamUser::getTeamId).collect(Collectors.toList());

        //获取全部的teams
        if(teamsId.isEmpty()){
            return null;
        }
        try{
            return getTeamDTOsByTeamIds(teamsId);
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private List<TeamDTO> getTeamDTOsByTeamIds(List<BigInteger> teamIds) throws Exception {
        // get all teams and put them into maps
        List<Team> teams = teamMapper.selectList(new QueryWrapper<Team>().in("team_id", teamIds));
        HashMap<BigInteger, TeamDTO> teamMap = new HashMap<>();
        List<TeamDTO> returnList = new ArrayList<>();
        for (Team team: teams){
            String creator = userMapper.selectOne(new QueryWrapper<User>().eq("user_id", team.getCreator())).getUsername();
            TeamDTO newTeam = new TeamDTO().setTeamId(team.getTeamId()).setTeamName(team.getTeamName()).setCreator(creator)
                    .setDescription(team.getDescription()).setCreatTime(team.getCreateTime()).setAdmins(new ArrayList<>()).setMembers(new ArrayList<>());
            teamMap.put(team.getTeamId(), newTeam);
            returnList.add(newTeam);
        }
        // get user relationship
        List<HashMap<String, Object>> teamUserMaps = teamMapper.getTeamUserRelationships(teams);
        for(HashMap<String, Object> map: teamUserMaps){
            String identity = (String)map.get("identity");
            if (identity != null) {
                String userId = ((Long)map.get("user_id")).toString();
                BigInteger teamId = BigInteger.valueOf((Long)map.get("team_id"));
                if ("admin".equals(identity))
                    teamMap.get(teamId).getAdmins().add(userId);
                if ("member".equals(identity))
                    teamMap.get(teamId).getMembers().add(userId);
            }
        }
        // get all related task
        for(TeamDTO team: teamMap.values()){
            team.setTeamTasks(taskService.queryTask(new QueryTaskDTO().setTeamId(team.getTeamId())));
        }
        return returnList;

    }

    @Override
    public UserDTO getProfile(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username)).toUserDTO();
    }

    @Override
    public List<Role> findRolesByUserName(String username) {
        return userMapper.findRolesByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }


    @Override //todo
    public Result register(RegisterInfoDTO registerInfoDTO) {
        userMapper.register(registerInfoDTO);
        return Result.succ("注册成功");
    }

    @Override
    public Result addTag(TagDTO params){
        BigInteger uId = userMapper.getIdByName(params.getUsername());
        Tag tag = new Tag().setTagName(params.getTagName()).setUserId(uId);
        tagMapper.insert(tag);
        return Result.succ("添加成功");
    }

    public List<Tag> selectTags(TagDTO params){
        BigInteger uId = userMapper.getIdByName(params.getUsername());
        QueryWrapper<Tag> wrapper = new QueryWrapper<Tag>().eq("user_id", uId);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public void editUser(EditUserDTO params){
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username", params.getUsername());
        if (params.getPassword() != null) wrapper.set("password", params.getPassword());
        if (params.getFirstName() != null) wrapper.set("first_name", params.getFirstName());
        if (params.getLastName() != null) wrapper.set("last_name", params.getLastName());
        if (params.getEmail() != null) wrapper.set("email", params.getEmail());
        if (params.getPhone() != null) wrapper.set("phone", params.getPhone());
        userMapper.update(user, wrapper);
    }

    @Override
    public void bindGithub(String username, String githubName) {
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username",username);
        wrapper.set("github_name",githubName);
        userMapper.update(user,wrapper);
    }

    @Override
    public void unbindGithub(String username) {
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username",username);
        wrapper.set("github_name",null);
        userMapper.update(user,wrapper);
    }

    @Override
    public User getUserByGithub(String githubName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("github_name",githubName);
        return userMapper.selectOne(queryWrapper);
    }

}
