package com.hungry.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private BigInteger teamId;
    @NonNull
    private String teamName;
    private String description;
    private String createTime; // 时间戳 可能有bug（import）
    private BigInteger creator;

}
