package com.hungry.taskmanager.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hungry.taskmanager.entity.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class MicrosoftUtil {
    private static String getMicrosoftAccessToken(String code) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://login.microsoftonline.com/common/oauth2/v2.0/token");
        urlConnection = (HttpURLConnection) url.openConnection();
        // 设置请求方式
        urlConnection.setRequestMethod("POST");
        // 设置数据类型
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 设置允许输入输出
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        // 设置不用缓存
        urlConnection.setUseCaches(false);

        urlConnection.connect();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8));
        // 写入传递参数,格式为a=b&c=d
        out.print("client_id=70f26831-fd49-4b56-a707-3a1ba1ae66d6&grant_type=authorization_code&scope=Calendars.ReadWrite&redirect_uri=http://localhost:8080/outlook/login&client_secret=AqB7Q~N2MQPr5h1Dp0RH0VTn2JuNY0T4slG09&code="
                + code);
        out.flush();

        int resultCode = urlConnection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == resultCode) {
            StringBuilder stringBuffer = new StringBuilder();
            String readLine;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((readLine = responseReader.readLine()) != null) {
                stringBuffer.append(readLine);
            }
            responseReader.close();
            JSONObject j = JSONObject.parseObject(stringBuffer.toString());
            return j.getString("access_token");
        }
        out.close();
        return null;
    }

    private static String getCalendarString(String accessToken) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        String urlNameString = "https://graph.microsoft.com/v1.0/me/events";
        URL realUrl = new URL(urlNameString);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        //设置超时时间
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(15000);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + accessToken);

        // 设置通用的请求属性
        for (Map.Entry<String, String> entry : header.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : map.keySet()) {
            System.out.println(key + "--->" + map.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应，设置utf8防止中文乱码
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }

    private static List<JSONObject> getMicrosoftEventByCode(String code) throws IOException {
        String accessToken = getMicrosoftAccessToken(code);
        String calendarString = getCalendarString(accessToken);
        JSONObject originJson = JSONObject.parseObject(calendarString);
        JSONArray jsonArray = originJson.getJSONArray("value");
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (Object o : jsonArray) {
            jsonObjects.add((JSONObject) o);
        }
        return jsonObjects;
    }

    private static Task microSoftEventToTask(JSONObject microsoft) {
        Task task = new Task();
        task.setTaskName(microsoft.getString("subject"));
        String startString = microsoft.getJSONObject("start").getString("dateTime");
        task.setCreateDate(stringToLocalDateTime(startString));//start time
        String endString = microsoft.getJSONObject("end").getString("dateTime");
        LocalDateTime due = stringToLocalDateTime(endString);
        task.setDueDate(due);
        int remindBefore = microsoft.getInteger("reminderMinutesBeforeStart");
        task.setRemindDate(due.minusMinutes(remindBefore));
        task.setDescription(microsoft.getString("bodyPreview"));
        task.setLocation(microsoft.getJSONObject("location").getString("displayName"));
        return task;
    }

    private static LocalDateTime stringToLocalDateTime(String string) {
        int year = Integer.parseInt(string.substring(0, 4));
        int mouth = Integer.parseInt(string.substring(5, 7));
        int day = Integer.parseInt(string.substring(8, 10));
        int hour = Integer.parseInt(string.substring(11, 13));
        int minute = Integer.parseInt(string.substring(14, 16));
        int second = Integer.parseInt(string.substring(17, 19));
        return LocalDateTime.of(year, mouth, day, hour, minute, second);
    }

    public static List<Task> getTasksByCode(String code) throws IOException {
        List<JSONObject> jsonObjects = getMicrosoftEventByCode(code);
        List<Task> tasks = new ArrayList<>();
        for (JSONObject jsonObject : jsonObjects) {
            tasks.add(microSoftEventToTask(jsonObject)) ;
        }
        return tasks;
    }
}
