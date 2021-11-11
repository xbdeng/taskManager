package com.hungry.taskmanager.service;


import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.dto.RegisterInfoDTO;
import com.hungry.taskmanager.entity.Role;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.entity.Result;

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
}
