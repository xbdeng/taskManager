package com.hungry.taskmanager.entity;

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
    private String username;
    @NonNull
    private String password;
    private String salt;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
