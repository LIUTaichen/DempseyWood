package com.dempseywood.operatordatacollector.models;

import java.util.Date;
public class StartHaulRequest {

    private String equipment;
    private String task;
    private String operator;
    private Double loadLatitude;
    private Double loadLongitude;
    private Date loadTime;
    private String imei;
    private String uuid;


    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getLoadLatitude() {
        return loadLatitude;
    }

    public void setLoadLatitude(Double loadLatitude) {
        this.loadLatitude = loadLatitude;
    }

    public Double getLoadLongitude() {
        return loadLongitude;
    }

    public void setLoadLongitude(Double loadLongitude) {
        this.loadLongitude = loadLongitude;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }



    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "StartHaulRequest{" +
                "equipment='" + equipment + '\'' +
                ", task='" + task + '\'' +
                ", operator='" + operator + '\'' +
                ", loadLatitude=" + loadLatitude +
                ", loadLongitude=" + loadLongitude +
                ", loadTime=" + loadTime +
                ", imei='" + imei + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
