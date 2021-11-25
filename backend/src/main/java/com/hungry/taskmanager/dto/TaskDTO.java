package com.hungry.taskmanager.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hungry.taskmanager.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    // attributes necessary for inserting a task
    private BigInteger taskId;
    private String creator;
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
    private List<TaskDTO> subTasks;
    @TableField(exist=false)
    private List<String> tags;
    @TableField(exist = false)
    private List<String> members;
}
