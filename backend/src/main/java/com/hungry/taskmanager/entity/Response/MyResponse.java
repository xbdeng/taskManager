package com.hungry.taskmanager.entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {
    String temp;

    public static MyResponse success() {
        return new MyResponse("success");
    }

    public static MyResponse permissionDenied() {
        return new MyResponse("permission denied");
    }

}
