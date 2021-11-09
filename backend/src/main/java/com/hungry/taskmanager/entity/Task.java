package com.hungry.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private BigInteger taskId;
    private BigInteger userId;
    private String title;
    private String description;
    private Integer type;
    @TableField(exist=false)
    private List<Tag> tags;
    private Integer privilege;
    @TableField(exist=false)
    private List<Task> subTask;
    private LocalDateTime createDate;
    private LocalDateTime dueDate;
    private Integer status;
    @TableField(exist=false)
    private BigInteger fatherTask;
}
