package com.hungry.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private BigInteger messageId;
    private String usernameFrom;
    private String usernameTo;
    private String type;
    private Integer status;
}
