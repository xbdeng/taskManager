package com.hungry.taskmanager.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {
    private String from;
    private Integer type;
    private String groupName;

    public String toJsonString(){
        return JSON.toJSONString(this);
    }
}
