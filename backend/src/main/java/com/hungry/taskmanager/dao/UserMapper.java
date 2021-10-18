package com.hungry.taskmanager.dao;

import com.hungry.taskmanager.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Insert("insert into user(user_name, password, user_role, user_permission) values(#{userName}, #{password},#{userRole},#{userPermission})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insert(User user);

    @Select("select * from user where user_name =#{username}")
    User selectByUserName(String username);
}
