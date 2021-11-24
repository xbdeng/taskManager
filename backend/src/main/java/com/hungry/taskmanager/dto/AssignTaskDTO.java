package com.hungry.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskDTO {
    List<String> usernames;
    BigInteger taskId;
}
