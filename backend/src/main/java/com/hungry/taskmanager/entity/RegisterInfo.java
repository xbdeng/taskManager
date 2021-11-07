package com.hungry.taskmanager.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfo {
    @NonNull
    @ApiModelProperty(required = true)
    private String username;
    @NonNull
    @ApiModelProperty(required = true)
    private String password;
    private String salt;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
