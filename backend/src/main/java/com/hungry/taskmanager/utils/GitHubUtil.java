package com.hungry.taskmanager.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class GitHubUtil {
    private static final String accessTokenUrl = "https://github.com/login/oauth/access_token?client_id=Iv1.187f346cb4978b94&client_secret=f152c15e30334c9b2357d7fd37075ba6e6adceb4&code=";

    public static String getGithubAccessToken(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        RestTemplate request = new RestTemplate();
        String accessTokenUrl_ = accessTokenUrl + code;
        String s = request.postForObject(accessTokenUrl_, String.class, String.class, map);
        System.out.println("accessTokenUrl返回的数据:" + s);
        String[] str = s.split("&");
        String access_token = str[0].substring(str[0].indexOf("=")).substring(1);
        getGithubUserName(access_token);
        return access_token;
    }

    public static String getGithubUserName(String accessToken) {
        String url = "https://api.github.com/user";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "token " + accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        result.getBody();
        String githubUserName = JSON.parseObject(result.getBody()).getString("login");
        System.out.println(githubUserName);
        return githubUserName;
    }
}
