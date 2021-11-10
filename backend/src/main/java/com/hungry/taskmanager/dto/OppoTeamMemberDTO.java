package com.hungry.taskmanager.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OppoTeamMemberDTO {
    @ApiModelProperty(required = true)
    BigInteger teamId;
    @ApiModelProperty(required = true)
    List<String> userName;
}
