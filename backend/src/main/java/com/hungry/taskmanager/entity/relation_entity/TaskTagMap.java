package com.hungry.taskmanager.entity.relation_entity;

import com.hungry.taskmanager.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskTagMap {
    private BigInteger taskId;
    private Tag tag;
}
