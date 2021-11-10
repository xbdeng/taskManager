package com.hungry.taskmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamDTO {
    @ApiModelProperty(required = true)
    String teamName;
    @ApiModelProperty(required = true)
    String creatorName;
    List<String> membersName;
    String description;
}
