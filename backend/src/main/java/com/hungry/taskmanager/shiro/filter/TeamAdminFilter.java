//package com.hungry.taskmanager.shiro.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.hungry.taskmanager.service.TeamService;
//import com.hungry.taskmanager.service.UserService;
//import com.hungry.taskmanager.utils.JWTUtil;
//import org.apache.shiro.web.filter.AccessControlFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.stream.Collectors;
//
//@Component
//public class TeamAdminFilter extends AccessControlFilter {
//
//    private static TeamService teamService;
//
//    private static UserService userService;
//
//    @Resource
//    public void setUserService(UserService userService){
//        TeamAdminFilter.userService = userService;
//    }
//
//    @Resource
//    public void setTeamService(TeamService teamService){
//        TeamAdminFilter.teamService = teamService;
//    }
//
//    //验证是否有管理员或群主权限
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
//            if (teamService.isAdmin(userId, teamId)) {
//                System.out.println("你有权限管理本群");
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
//        System.out.println("你无权管理本群");
//        return false;
//    }
//}
