package com.hungry.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EditTaskNameDTO {
    BigInteger taskId;
    String taskName;
}
