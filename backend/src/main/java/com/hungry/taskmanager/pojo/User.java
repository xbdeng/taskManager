package com.hungry.taskmanager.pojo;

import lombok.Data;

import java.io.Serializable;

@Data //自动get set
public class User implements Serializable {

    private String userId;
    private String password;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String userRole;
    private String userPermission;

    public User() {
    }

//    public User(String userName,String password,String firstName, String lastName, String email, String phone, String userRole, String userPermission) {
//        this.userId = userId;
//        this.password = password;
//        this.userName = userName;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phone = phone;
//        this.userRole = userRole;
//        this.userPermission = userPermission;
//    }
}
