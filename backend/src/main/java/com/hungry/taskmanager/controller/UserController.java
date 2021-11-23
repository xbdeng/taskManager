package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.Task;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.service.UserService;
import com.hungry.taskmanager.utils.GitHubUtil;
import com.hungry.taskmanager.utils.JWTUtil;
import com.hungry.taskmanager.utils.MicrosoftUtil;
import com.hungry.taskmanager.utils.RedisUtil;
import com.hungry.taskmanager.entity.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "√")
    public Result register(@RequestBody RegisterInfoDTO registerInfoDTO) {
        try {
            return userService.register(registerInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("checkname")
    @ApiOperation(value = "检查用户名是否被占用", notes = "√")
    public Result checkName(@RequestBody String username) {
        try {
            username = JSON.parseObject(username).getString("username");
            User u = userService.findByUserName(username);
            if (u == null) {
                return Result.succ("名称可以使用");
            } else {
                return Result.fail(202, "名称被占用", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }


    @PostMapping("/login")
    @ApiResponse(code = 200, message = "若登录成功，data直接是一个token字符串")
    @ApiOperation(value = "用户登录", notes = "√")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.getUserByPass(loginDTO.getUsername(), loginDTO.getPassword());
        Assert.notNull(user, "用户名或密码错误");
        long currentTimeMillis = System.currentTimeMillis();
        String token = JWTUtil.createToken(user.getUsername(), currentTimeMillis);
        redisUtil.set(loginDTO.getUsername(), currentTimeMillis, 30 * 60); //放入缓存（登录）
        return new Result<String>(200, "登陆成功", token);
    }

    @PostMapping("/loginbygithub")
    @ApiResponse(code = 200, message = "返回message为username")
    @ApiOperation(value = "github登录", notes = "√")
    public Result loginByGithub(@RequestBody String code) {
        try {
            code = JSON.parseObject(code).getString("code");
            String AccessToken = GitHubUtil.getGithubAccessToken(code);
            String gitHubUserName = GitHubUtil.getGithubUserName(AccessToken);
            User user = userService.getUserByGithub(gitHubUserName);
            if (user == null) {
                return Result.fail(201, "github账号未绑定，请先注册", null);
            }

            long currentTimeMillis = System.currentTimeMillis();
            String token = JWTUtil.createToken(user.getUsername(), currentTimeMillis);
            redisUtil.set(user.getUsername(), currentTimeMillis, 30 * 60); //放入缓存（登录）
            return new Result<String>(200, user.getUsername(), token);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/logout")
    @RequiresAuthentication //需要登录才能操作
    @ApiOperation(value = "用户登出", notes = "√")
    public Result logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            redisUtil.del(username);
            return Result.succ("登出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }


    @PostMapping("addressbook")
    @RequiresAuthentication
    @ApiOperation(value = "请求用户的通信录", notes = "√")
    public Result<List<UserDTO>> addressbook(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            List<UserDTO> friends = userService.getAddressBook(username);
            return new Result<List<UserDTO>>(200, "请求成功", friends);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("myteams")
    @ApiOperation(value = "请求用户加入的组", notes = "√")
    @RequiresAuthentication
    public Result<List<TeamDTO>> myteams(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            List<TeamDTO> teams = userService.getTeams(username);
            return new Result<>(200, "请求成功", teams);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("myteams/admin")
    @ApiOperation(value = "请求用户创建和管理的组", notes = "√")
    @RequiresAuthentication
    public Result<List<TeamDTO>> adminTeams(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            List<TeamDTO> teams = userService.getAdminTeams(username);
            return new Result<>(200, "请求成功", teams);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("profile")
    @RequiresAuthentication
    @ApiOperation(value = "请求用户主页信息", notes = "√")
    public Result<UserDTO> profile(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            UserDTO me = userService.getProfile(username);
            return new Result<>(200, "请求成功", me);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/addtag")
    @RequiresAuthentication
    @ApiOperation(value = "add a tag", notes = "√")
    public Result addTag(@RequestBody TagDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            return userService.addTag(params.setUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/selecttags")
    @RequiresAuthentication
    @ApiOperation(value = "select a tag", notes = "√")
    public Result<List<Tag>> selectTags(@RequestBody TagDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            List<Tag> tags = userService.selectTags(params.setTagName(username));
            return new Result<List<Tag>>(200, "select tag success", tags);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/edituser")
    @ApiOperation(value = "edit a user", notes = "√")
    @RequiresAuthentication
    public Result editUser(@RequestBody EditUserDTO params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            userService.editUser(params.setUsername(username));
            return Result.succ("编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/bindgithub")
    @ApiOperation(value = "绑定GitHub", notes = "√")
    @ApiResponse(code = 200, message = "返回data为username")
    @RequiresAuthentication
    public Result bindGithub(@RequestBody String code, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            code = JSON.parseObject(code).getString("code");
            String AccessToken = GitHubUtil.getGithubAccessToken(code);
            String gitHubUserName = GitHubUtil.getGithubUserName(AccessToken);
            userService.bindGithub(username, gitHubUserName);
            return new Result(200, "绑定成功", username);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/unbundgithub")
    @RequiresAuthentication
    @ApiOperation(value = "解绑GitHub", notes = "√")
    public Result unbundGithub(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            userService.unbindGithub(username);
            return Result.succ("解绑成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/removefriend")
    @RequiresAuthentication
    @ApiOperation(value = "delete a friend")
    public Result<String> removeFriend(@RequestBody String friend, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            String friendName = JSONObject.parseObject(friend).getString("username");
            userService.removeFriend(username, friendName);
            return new Result(200, "remove a friend successfully", "");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

    @PostMapping("/getmicrosofttask")
    @RequiresAuthentication
    public Result<List<Task>> getMicrosoftTask(@RequestBody String code) {
        try {
            String code_ = JSONObject.parseObject(code).getString("code");
            List<Task> tasks = MicrosoftUtil.getTasksByCode(code_);
            return new Result<>(200, "请求成功", tasks);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "服务器错误", null);
        }
    }

}
