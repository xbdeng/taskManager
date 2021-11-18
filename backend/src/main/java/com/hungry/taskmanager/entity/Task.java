package com.hungry.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
    // attributes necessary for inserting a task
    @TableId(type= IdType.AUTO)
    private BigInteger taskId;
    private BigInteger creator;
    private String taskName;
    private String description;
    private BigInteger type;
    private LocalDateTime createDate;
    private LocalDateTime dueDate;
    private Integer status;
    private BigInteger fatherTask;
    private Integer privilege;
    // other attributes
    @TableField(exist=false)
    private List<Task> subTask;
    @TableField(exist=false)
    private List<String> tags;

    public void updateDate(){
        if (dueDate != null && LocalDateTime.now().isAfter(dueDate)){
            status = 2;
        }
    }
}
