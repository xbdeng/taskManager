package com.hungry.taskmanager.dao;

import com.hungry.taskmanager.pojo.User;

import java.util.List;

public interface UserMapper {

    List<User> selectUser();
}
