package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.service.UserService;
import com.hungry.taskmanager.utils.JWTUtil;
import com.hungry.taskmanager.utils.RedisUtil;
import com.hungry.taskmanager.entity.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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
     * 验证码方法
     */
//    @RequestMapping("getImage")//todo
//    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
//        //生成验证码
//        String code = VerifyCodeUtils.generateVerifyCode(4);
//        //验证码放入session
//        session.setAttribute("code",code);
//        //验证码存入图片
//        ServletOutputStream os = response.getOutputStream();
//        response.setContentType("image/png");
//        VerifyCodeUtils.outputImage(220,60,os,code);
//    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes = "后端已测试通过")
    public Result register(@RequestBody RegisterInfoDTO registerInfoDTO) {
        return userService.register(registerInfoDTO);
    }




    @PostMapping("/login")
    @ApiResponse(code = 200,message = "若登录成功，data直接是一个token字符串")
    @ApiOperation(value = "用户登录",notes = "后端已测试通过")
    public Result<String> login(@RequestBody LoginDTO loginDTO){
        User user=userService.getUserByPass(loginDTO.getUsername(), loginDTO.getPassword());
        Assert.notNull(user,"用户名或密码错误");
        long currentTimeMillis = System.currentTimeMillis();
        String token= JWTUtil.createToken(user.getUsername(),currentTimeMillis);
        redisUtil.set(loginDTO.getUsername(),currentTimeMillis,30*60); //放入缓存（登录）
        return new Result<String>(200,"登陆成功",token);
    }

    @PostMapping("/logout")
    @RequiresAuthentication //需要登录才能操作
    @ApiOperation(value = "用户登出",notes = "后端已测试通过")
    public Result logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        redisUtil.del(username);
        return Result.succ("登出成功");
    }


    @PostMapping("addressbook")
    @RequiresAuthentication
    @ApiOperation(value = "请求用户的通信录",notes = "后端已测试通过")
    public Result<List<UserDTO>> addressbook(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        List<UserDTO> friends = userService.getAddressBook(username);
        return new Result<List<UserDTO>>(200,"请求成功",friends);
    }

    @PostMapping("myteams")
    @ApiOperation(value = "请求用户加入的组")
    // todo mybatis
    public Result<List<TeamDTO>> myteams(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        List<TeamDTO> teams = userService.getTeams(username);
        return new Result<>(200,"请求成功",teams);
    }

    @PostMapping("myteams/admin")
    @ApiOperation(value = "请求用户创建和管理的组")
    // todo mybatis
    public Result<List<TeamDTO>> adminTeams(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        List<TeamDTO> teams = userService.getAdminTeams(username);
        return new Result<>(200,"请求成功",teams);
    }

    @PostMapping("profile")
    @RequiresAuthentication
    @ApiOperation(value = "请求用户主页信息",notes = "后端已测试通过")
    public Result<UserDTO> profile(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        UserDTO me = userService.getProfile(username);
        return new Result<>(200,"请求成功",me);
    }

    @PostMapping("/addtag")
    @RequiresAuthentication
    @ApiOperation(value="add a tag",notes = "前后端测试通过")
    public Result<String> addTag(@RequestBody TagDTO params, HttpServletRequest request){
        try{
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            userService.addTag(params.setUsername(username));
        }catch(Exception e){
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully add a tag", "");
    }

    @PostMapping("/selecttags")
    @RequiresAuthentication
    @ApiOperation(value="select a tag",notes = "前后端测试通过")
    public Result<List<Tag>> selectTags(@RequestBody TagDTO params, HttpServletRequest request){
        Result<List<Tag>> result = new Result<>();
        try{
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
//            result.setData(userService.selectTags(params.setTagName(username)));
            List<Tag> tags =  userService.selectTags(params.setTagName(username));
            return new Result<List<Tag>>(200,"select tag success",tags);
        }catch(Exception e){
            e.printStackTrace();
            return new Result<>(500, "server error", new ArrayList<>());
        }
    }



}
