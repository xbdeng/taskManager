package com.hungry.taskmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDTO {
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @ApiModelProperty(hidden =  true)
    private String username;
}
