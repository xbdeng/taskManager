package com.hungry.taskmanager.service;


import com.hungry.taskmanager.dto.TagDTO;
import com.hungry.taskmanager.dto.TeamDTO;
import com.hungry.taskmanager.dto.UserDTO;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.dto.RegisterInfoDTO;

import java.util.List;

public interface UserService {
    //注册用户方法
    Result register(RegisterInfoDTO registerInfoDTO);


    //根据用户名查询业务的方法
    User findByUserName(String username);

    //根据用户名查询所有角色
    List<Role> findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);

    //根据用户名和密码查询用户
    User getUserByPass(String username, String password);

    //获取通信录
    List<UserDTO> getAddressBook(String username);

    //获取用户的组
    List<TeamDTO> getTeams(String username);

    //获取用户管理的组
    List<TeamDTO> getAdminTeams(String username);

    //获取个人信息
    UserDTO getProfile(String username);

    void addTag(TagDTO params);

    List<Tag> selectTags(TagDTO params);
}
