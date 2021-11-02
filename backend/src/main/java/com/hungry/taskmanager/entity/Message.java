package com.hungry.taskmanager.entity;

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
    private BigInteger messageId;
    private User sender;
    private User receiver;
    private String content;
    private Boolean isRead;
}
