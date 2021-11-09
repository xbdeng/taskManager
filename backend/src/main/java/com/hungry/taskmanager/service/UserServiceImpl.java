package com.hungry.taskmanager.service;

import com.hungry.taskmanager.dao.UserDAO;
import com.hungry.taskmanager.entity.Perms;
import com.hungry.taskmanager.entity.Request.RegisterInfo;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {


    //todo 用@Autowired爆红
    @Resource
    private UserDAO userDAO;


    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userDAO.findPermsByRoleId(id);
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDAO.findRolesByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userDAO.findByUserName(username);
    }


//    @Override //todo
    public MyResponse register(RegisterInfo registerInfo) {
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        registerInfo.setSalt(salt);
        //3.明文密码进行md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(registerInfo.getPassword(),salt,1024);
        registerInfo.setPassword(md5Hash.toHex());

        userDAO.register(registerInfo);
        return new MyResponse("todo");
    }

}
