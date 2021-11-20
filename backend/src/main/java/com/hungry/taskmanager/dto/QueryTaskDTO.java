package com.hungry.taskmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryTaskDTO {
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
    private BigInteger teamId;
    private Integer type;
    private Integer status;
    @ApiModelProperty(hidden = true)
    private BigInteger fatherTask;
    private String currentDate;
    private Integer timeRange;
    private Integer scheduledTask;
    @ApiModelProperty(hidden = true)
    private LocalDateTime requiredDate;
}
