package com.dempseywood.operatordatacollector.helpers;

import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;
import com.dempseywood.operatordatacollector.models.UpdateTaskRequest;

import java.util.Date;

/**
 * Created by Jason.Liu on 5/02/2018.
 */

public class UpdateTaskRequestBuilder {

    public static UpdateTaskRequest build(Haul haul, Task newTask){
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setHaulUuid(haul.getUuid());
        request.setId(haul.getId());
        request.setImei(haul.getImei());
        request.setTask(newTask.getName());
        request.setTimestamp(new Date());
        return request;
    }
}
