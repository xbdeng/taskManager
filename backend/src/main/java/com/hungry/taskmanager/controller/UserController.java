package com.hungry.taskmanager.controller;

import com.hungry.taskmanager.entity.RegisterInfo;
import com.hungry.taskmanager.entity.Response.MyResponse;
import com.hungry.taskmanager.entity.User;
import com.hungry.taskmanager.service.UserService;
import com.hungry.taskmanager.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "*")
public class UserController {

    @Resource
    private UserService userService;

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
    public MyResponse register(@RequestBody RegisterInfo registerInfo) {
        return userService.register(registerInfo);
    }


    /**
     * 退出登录
     */
//    @RequestMapping("logout") //todo
//    public String logout() {
//        Subject subject = SecurityUtils.getSubject();
//        subject.logout();//退出用户
//        return "redirect:/user/loginview";
//    }

    /**
     * 用来处理身份认证
     *
     * @param username
     * @param password
     * @return
     */
//    @RequestMapping("login") //todo
//    public String login(String username, String password,String code,HttpSession session) {
//        //比较验证码
//        String codes = (String) session.getAttribute("code");
//        try {
//            if (codes.equalsIgnoreCase(code)){
//                //获取主体对象
//                Subject subject = SecurityUtils.getSubject();
//                    subject.login(new UsernamePasswordToken(username, password));
//                    return "redirect:/index";
//            }else{
//                throw new RuntimeException("验证码错误!");
//            }
//        } catch (UnknownAccountException e) {
//            e.printStackTrace();
//            System.out.println("用户名错误!");
//        } catch (IncorrectCredentialsException e) {
//            e.printStackTrace();
//            System.out.println("密码错误!");
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        return "redirect:/user/loginview";
//    }
    @PostMapping("login")
    public MyResponse login(String username, String password, HttpSession httpSession) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
        } catch (UnknownAccountException e) {
//            e.printStackTrace();
            System.out.println("用户名错误");
        } catch (IncorrectCredentialsException e) {
//            e.printStackTrace();
            System.out.println("密码错误!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new MyResponse("todo");
    }
}
