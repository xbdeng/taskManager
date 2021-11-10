package com.hungry.taskmanager.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer userId;
    private String username;
    private String password;
    private String salt;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    //定义角色集合
    private List<Role> roles;
    private List<Perms> perms;
    private List<Task> tasks;


}
