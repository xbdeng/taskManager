package com.hungry.taskmanager.entity.post_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryTaskFilter {
    private String username;
    private Integer privilege;
    private String tag;
    private String dueDate;
}
