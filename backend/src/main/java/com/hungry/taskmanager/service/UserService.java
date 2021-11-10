package com.hungry.taskmanager.service;


import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.dto.RegisterInfoDTO;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.User;

import java.util.List;

public interface UserService {
    //注册用户方法
    MyResponse register(RegisterInfoDTO registerInfoDTO);


    //根据用户名查询业务的方法
    User findByUserName(String username);

    //根据用户名查询所有角色
    User findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);
}
