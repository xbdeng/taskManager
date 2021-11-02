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
public class Group {
    private BigInteger groupId;
    @NonNull
    private String groupName;
    private String description;
    private Timestamp creatTime; // 时间戳 可能有bug（import）
    private User creator;

    private List<User> members;

}
