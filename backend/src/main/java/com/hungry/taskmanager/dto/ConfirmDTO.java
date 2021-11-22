package com.hungry.taskmanager.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmDTO {
    private String from;
    private BigInteger messageId;
    private Integer operation;
}
