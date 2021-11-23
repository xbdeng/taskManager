package com.hungry.taskmanager.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
public class InvitationDTO {
    @ApiModelProperty(hidden=true)
    private String from;
    @JSONField(serialize =false)
    private List<String> usernameTo;
    private Integer type;
    private BigInteger teamId;
}
