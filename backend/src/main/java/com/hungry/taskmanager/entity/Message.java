package com.hungry.taskmanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @JSONField(deserialize=false)
    private BigInteger messageId;
    private String usernameFrom;
    private String usernameTo;
    private String content;
    @JSONField(deserialize=false)
    private Boolean isRead;
}
