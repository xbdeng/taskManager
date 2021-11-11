package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dao.UserMapper;
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
    public List<Role> findRolesByUserName(String username) {
        return userMapper.findRolesByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }


    @Override //todo
    public Result register(RegisterInfoDTO registerInfoDTO) {
        //1.生成随机盐
//        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
//        registerInfoDTO.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
//        Md5Hash md5Hash = new Md5Hash(registerInfoDTO.getPassword(),salt,1024);
//        registerInfoDTO.setPassword(md5Hash.toHex());

        userMapper.register(registerInfoDTO);
        return Result.succ("注册成功");
    }

}
