package com.hungry.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private BigInteger userId;
    private String username;
    private String password;
    private String salt;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    //定义角色集合
    @TableField(exist=false)
    private List<Role> roles;
  
    @TableField(exist=false)
    private List<Perms> perms;

    @TableField(exist=false)
    private List<Task> tasks;


}
