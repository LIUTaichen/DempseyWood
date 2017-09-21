package com.dempseywood.operatordatacollector.database.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by musing on 04/08/2017.
 */
@Entity
public class EquipmentStatus {
    @PrimaryKey
    private Integer id;

    private String equipment;

    private String operator;

    private String status;

    private String task;

    private Date timestamp;

    private Double latitude;

    private Double longitude;
    private String imei;
    private boolean isSent;




    public Integer getId() {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(boolean sent) {
        isSent = sent;
    }
}
