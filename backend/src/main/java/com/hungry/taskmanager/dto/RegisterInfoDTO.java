package com.hungry.taskmanager.dto;

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
public class RegisterInfoDTO {
    @ApiModelProperty(required = true)
    private String username;
    @ApiModelProperty(required = true)
    private String password;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
