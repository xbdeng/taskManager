package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.MyResponse;
import com.hungry.taskmanager.pojo.User;
import com.hungry.taskmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


@RequestMapping("/api/user")
@RestController
public class UserController {
    /** 存储Token的H Headers Key **/
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    @Resource
    UserService userService;

    /** 用户登录接口
     * @param user 用户登录的用户名和密码
     * @return 用户Token和角色
     */
    @PostMapping(value = "/login")
    public MyResponse login(@RequestBody final User user, ServletResponse response) {
        MyResponse myResponse = userService.login(user);
        // 如果登录成功
        // 将 Token 写入到 Response 的 Header，方便前端刷新 Token 从 Header 取值
        if ("SUCCESS".equals(myResponse.getStatus())) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.addHeader(AUTHORIZATION_HEADER, myResponse.getMessage());
        }
        return myResponse;
    }

    @PostMapping("/register")
    public MyResponse register(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/hello")
    public String hello() {
        return "已经登录的用户可见";
    }
}

