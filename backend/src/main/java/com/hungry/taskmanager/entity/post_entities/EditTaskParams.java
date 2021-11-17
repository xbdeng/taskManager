package com.hungry.taskmanager.entity.post_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EditTaskParams extends CreateTaskParams{
    // other parameters necessary for modifying a task
    private BigInteger fatherTask;
    private List<BigInteger> subtasks;
    private Integer status;
    private Long taskId;
}
