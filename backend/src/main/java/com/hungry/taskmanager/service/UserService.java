package com.hungry.taskmanager.service;

import com.hungry.taskmanager.MyResponse;
import com.hungry.taskmanager.dao.UserMapper;
import com.hungry.taskmanager.pojo.User;
import com.hungry.taskmanager.shiro.TokenUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserService {
    /** Hash 加密的盐 **/
    private final String SALT = "#4d1*dlmmddewd@34%";
    @Resource
    private TokenUtils tokenUtils;
    @Resource private UserMapper userMapper;

    /** 用户登录 **/
    public MyResponse login(User user) {
        // 从 数据库查询用户信息
        User useri = userMapper.selectByUserName(user.getUserName());
        if (useri == null || useri.getUserName() == null || useri.getPassword() == null
                || useri.getUserRole() == null || useri.getUserPermission() == null) {
            return new MyResponse("error", "用户信息不存在");
        }
        String password = new SimpleHash("SHA-512", user.getPassword(), this.SALT).toString();
        if (!password.equals(useri.getPassword())) {
            return new MyResponse("error", "密码错误");
        }
        // 生成 Token
        return new MyResponse("SUCCESS",
                tokenUtils.createToken(useri));
    }

    /** 用户注册
     * @param user 用户注册信息
     * @return 用户注册结果
     */
    public MyResponse save(User user) throws DataAccessException {
        try {
            // 密码加密存储
            String password = new SimpleHash("SHA-512", user.getPassword(), this.SALT).toString();
            userMapper.insert(user);
        } catch (DataAccessException e) {
            return new MyResponse("ERROR", "已经存在该用户名或者用户昵称，或者用户权限出错");
        }
        return new MyResponse("SUCCESS", "用户新增成功");
    }
}

