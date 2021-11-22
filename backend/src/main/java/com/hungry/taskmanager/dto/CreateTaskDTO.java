package com.hungry.taskmanager.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreateTaskDTO {
    // parameters necessary for creating a task
    @ApiModelProperty(hidden = true)
    private String username;
    private String taskName;
    private String description;
    private List<String> tags;
    private Integer privilege;
    private Integer type;
    private String location;
    // attributes for team if type is team
    private BigInteger teamId;
    private String createDate;
    private String dueDate;
    private String remindDate;

}
