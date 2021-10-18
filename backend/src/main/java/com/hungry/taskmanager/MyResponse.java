package com.hungry.taskmanager;

import lombok.Data;

import java.io.Serializable;

//公共 Response 返回消息类
@Data
public class MyResponse implements Serializable {
    private static final long serialVersionUID = -2L;
    private String status;
    private String message;

    public MyResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

