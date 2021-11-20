package com.hungry.taskmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EditTaskDTO {
    @ApiModelProperty(hidden = true)
    private String username;
    private String taskName;
    private String description;
    private List<String> tags;
    private Integer privilege;
    private String createDate;
    private String dueDate;
    // other parameters necessary for modifying a task
    private BigInteger fatherTask;
    private List<BigInteger> subtasks;
    private Integer status;
    private Long taskId;
}
