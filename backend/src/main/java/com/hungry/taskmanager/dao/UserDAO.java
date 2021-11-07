package com.hungry.taskmanager.dao;


import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.entity.RegisterInfo;
import com.hungry.taskmanager.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserDAO {
    @Insert("insert into user(username, password, salt, first_name, last_name, email, phone) values(#{username}," +
            "#{password}," +
            "#{salt}," +
            "#{firstName}," +
            "#{lastName}," +
            "#{email}," +
            "#{phone})")
    void register(RegisterInfo registerInfo);//todo 异常处理

    User findByUserName(String username);


    //根据用户名查询所有角色
    User findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);
}

