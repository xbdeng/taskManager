package com.hungry.taskmanager.service;


import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
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

    Result addTag(TagDTO params);

    List<Tag> selectTags(TagDTO params);

    void editUser(EditUserDTO userDTO);

    //绑定github
    void bindGithub(String username, String githubName);

    //解绑github
    void unbindGithub(String username);

    //根据GitHub name 查找用户
    User getUserByGithub(String githubName);

    // add a friend
    void addFriend(BigInteger usernameA, BigInteger usernameB);

    boolean hasAFriend(BigInteger person, BigInteger friend);

    void removeFriend(String username, String friendName);

    void uploadImage(String username, String url);

    String getImage(String username);

    //模糊查询用户
    Result<List<UserDTO>> searchFriend(String name);

    //统计个人任务完成情况
    Result<PersonalStatisticsDTO> personalStatistics(String username);

    //验证邮箱
    Result verifyEmail(String username);

    boolean verifyCode(String username, String code);


}
