package com.hungry.taskmanager.entity.post_entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskParams {
    private String username;
    private String title;
    private String description;
    private List<String> tags;
    private Integer privilege;
    private Integer type;
    private String createTime;
    private String dueTime;
}
