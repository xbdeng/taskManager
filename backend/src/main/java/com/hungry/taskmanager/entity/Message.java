package com.hungry.taskmanager.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type= IdType.AUTO)
    private BigInteger messageId;
    private BigInteger sender;
    private BigInteger receiver;
    private String content;
    private String type;
    private Integer isRead;
}
