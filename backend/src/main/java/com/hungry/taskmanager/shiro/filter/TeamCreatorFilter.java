//package com.hungry.taskmanager.shiro.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.hungry.taskmanager.service.TeamService;
//import com.hungry.taskmanager.service.UserService;
//import com.hungry.taskmanager.utils.JWTUtil;
//import org.apache.shiro.web.filter.AccessControlFilter;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.stream.Collectors;
//
//public class TeamCreatorFilter extends AccessControlFilter {
//    @Resource
//    UserService userService;
//
//    @Resource
//    TeamService teamService;
//
//    //验证是否有群主权限
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        try {
//            String s = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//            JSONObject body = JSON.parseObject(s);
//            BigInteger teamId = body.getBigInteger("teamId");
//
//            String token = httpServletRequest.getHeader("Authorization");
//            String username = JWTUtil.getUsername(token);
//            BigInteger userId = userService.findByUserName(username).getUserId();
//
//            if (teamService.isCreator(userId, teamId)) {
//                System.out.println("你是本群群主");
//                return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        return false;
//    }
//}
