package com.hungry.taskmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {
    @ApiModelProperty(hidden=true)
    private String usernameFrom;
    private String usernameTo;
    private Integer type;
    private BigInteger grouId;
}
