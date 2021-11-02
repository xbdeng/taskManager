package com.hungry.taskmanager.entity;

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
public class Task {
    private BigInteger taskId;
    private BigInteger userId;
    private String title;
    private String description;

    private List<Tag> tags;
    private List<Task> subTask;
}
