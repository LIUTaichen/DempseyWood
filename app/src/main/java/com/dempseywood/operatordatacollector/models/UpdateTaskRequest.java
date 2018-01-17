package com.dempseywood.operatordatacollector.models;

import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
public class UpdateTaskRequest {

    private Integer id;
    private String imei;
    private String task;
    private String haulUuid;
    private Date timestamp;
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public String getHaulUuid() {
        return haulUuid;
    }

    public void setHaulUuid(String haulUuid) {
        this.haulUuid = haulUuid;
    }

    @Override
    public String toString() {
        return "UpdateTaskRequest{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", task='" + task + '\'' +
                ", HaulUuid='" + haulUuid + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
