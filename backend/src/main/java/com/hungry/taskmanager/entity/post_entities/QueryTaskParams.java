package com.hungry.taskmanager.entity.post_entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTaskParams {
    @ApiModelProperty(hidden = true)
    private String username;
    @ApiModelProperty(hidden = true)
    private BigInteger userId;
    private String taskName;
    private Integer privilege;
    private String tag;
    private String dueDate;
    private String createDate;
    private String teamName;
    private Integer type;
    private Integer status;
    @ApiModelProperty(hidden = true)
    private BigInteger fatherTask;
}
