package com.hungry.taskmanager.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.dto.RegisterInfoDTO;
import com.hungry.taskmanager.entity.Role;
import com.hungry.taskmanager.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into user(username, password, salt, first_name, last_name, email, phone) values(#{username}," +
            "#{password}," +
            "#{salt}," +
            "#{firstName}," +
            "#{lastName}," +
            "#{email}," +
            "#{phone})")
    void register(RegisterInfoDTO registerInfoDTO);//todo 异常处理

    User findByUserName(String username);


    //根据用户名查询所有角色
    List<Role> findRolesByUserName(String username);

    //根据角色id查询权限集合
    List<Perms> findPermsByRoleId(String id);

    // query id by username
    @Select("SELECT user_id FROM user WHERE username = #{username}")
    BigInteger getIdByName(String username);

    //根据用户名和密码查询用户
    @Select("SELECT * from user WHERE username = #{username} and password = #{password}")
    User getUserByPass(String username, String password);

}

