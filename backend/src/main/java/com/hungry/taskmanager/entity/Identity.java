package com.hungry.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

//group role
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Identity {
    private BigInteger identityId;
    private String identityName;
}
