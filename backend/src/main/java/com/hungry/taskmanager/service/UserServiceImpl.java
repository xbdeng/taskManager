package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.dto.TeamDTO;
import com.hungry.taskmanager.dto.UserDTO;
import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.dto.RegisterInfoDTO;
import com.hungry.taskmanager.entity.Role;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {


    //todo 用@Autowired爆红
    @Resource
    private UserMapper userMapper;


    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userMapper.findPermsByRoleId(id);
    }

    @Override
    public User getUserByPass(String username, String password) {
        return userMapper.getUserByPass(username,password);
    }

    @Override
    public List<UserDTO> getAddressBook(String username) {
        return null;//todo
    }

    @Override
    public List<TeamDTO> getTeams(String username) {
        return null;//todo
    }

    @Override
    public List<TeamDTO> getAdminTeams(String username) {
        return null; //todo
    }

    @Override
    public UserDTO getProfile(String username) {
        return null; //todo
    }

    @Override
    public List<Role> findRolesByUserName(String username) {
        return userMapper.findRolesByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }


    @Override //todo
    public Result register(RegisterInfoDTO registerInfoDTO) {
        userMapper.register(registerInfoDTO);
        return Result.succ("注册成功");
    }

}
