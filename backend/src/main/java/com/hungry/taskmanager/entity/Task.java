package com.hungry.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

// todo 把task和dto分离出来

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    // attributes necessary for inserting a task
    @TableId(type= IdType.AUTO)
    private BigInteger taskId;
    private BigInteger creator;
    private String taskName;
    private String description;
    private String location;
    private BigInteger type;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime dueDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime remindDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime finishDate;
    private Integer status;
    private BigInteger fatherTask;
    private Integer privilege;
    // other attributes
    @TableField(exist=false)
    private List<Task> subTasks;
    @TableField(exist=false)
    private List<String> tags;
    @TableField(exist = false)
    private List<String> members;

    public void updateDate(){
        if (dueDate != null && LocalDateTime.now().isAfter(dueDate)){
            status = 2;
        }
    }
}
