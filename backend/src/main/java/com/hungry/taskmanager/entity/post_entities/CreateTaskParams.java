package com.hungry.taskmanager.entity.post_entities;
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
public class CreateTaskParams {
    // parameters necessary for creating a task
    @ApiModelProperty(hidden = true)
    private String username;
    private String taskName;
    private String description;
    private List<String> tags;
    private Integer privilege;
    private Integer type;
    // attributes for team if type is team
    private String teamName;
    private List<String> members;
    private String createDate;
    private String dueDate;

}
