package com.hungry.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Tag;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.service.UserService;
import com.hungry.taskmanager.utils.JWTUtil;
import com.hungry.taskmanager.utils.RedisUtil;
import com.hungry.taskmanager.entity.Result;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    @RequiresAuthentication
    public Result<List<TeamDTO>> myteams(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);
        List<TeamDTO> teams = userService.getTeams(username);
        return new Result<>(200,"请求成功",teams);
    }

    @PostMapping("myteams/admin")
    @ApiOperation(value = "请求用户创建和管理的组", notes="passed")
    @RequiresAuthentication
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

    @PostMapping("/edituser")
    @ApiOperation(value="edit a user", notes="后端测试未通过")
    @RequiresAuthentication
    public Result<String> editUser(@RequestBody EditUserDTO params, HttpServletRequest request){
        try{
            String token = request.getHeader("Authorization");
            String username = JWTUtil.getUsername(token);
            userService.editUser(params.setUsername(username));
        }catch(Exception e){
            e.printStackTrace();
            return new Result<String>(500, "server error", "");
        }
        return new Result<String>(200, "successfully edit a user", "");
    }

    @PostMapping("/bindgithub")
    public Result bindGithub(@RequestBody String code,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String username = JWTUtil.getUsername(token);

        String AccessToken = getGithubAccessToken(code);
        String GitHubUserName = getGithubUserName(AccessToken);
        return null; //todo
    }

    private String getGithubAccessToken(String code){
        //请求路径
        String url = "https://github.com/login/oauth/access_token";
        //使用Restemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //提交参数设置
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id","Iv1.187f346cb4978b94");
        map.add("client_secret","f152c15e30334c9b2357d7fd37075ba6e6adceb4");
        map.add("code",code);

        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> requestBody = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        // 发送post请求，以String类型接收响应结果JSON字符串
        String result = restTemplate.postForObject(url, requestBody, String.class);
        return JSON.parseObject(result).getString("access_token");
    }

    private String getGithubUserName(String accessToken){
        String url = "https://api.github.com/user";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization","token "+accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,requestEntity,String.class);

        System.out.println(result);
        return result.toString(); //todo
    }

}
