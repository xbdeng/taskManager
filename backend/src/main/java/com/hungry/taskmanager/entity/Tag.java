package com.hungry.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private BigInteger tagId;
    private String tagName;
    private BigInteger userId;
    @TableField(exist = false)
    private BigInteger taskId;
}
