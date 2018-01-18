package com.dempseywood.operatordatacollector.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by musing on 26/09/2017.
 */
@Entity
public class Haul {
    @PrimaryKey
    private String uuid;
    private Integer id;
    private String operator;
    private String equipment;
    private String task;
    private Double loadLatitude;
    private Double loadLongitude;
    private Double unloadLatitude;
    private Double unloadLongitude;
    private Date loadTime;
    private Date unloadTime;
    private String imei;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

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

    public Double getUnloadLatitude() {
        return unloadLatitude;
    }

    public void setUnloadLatitude(Double unloadLatitude) {
        this.unloadLatitude = unloadLatitude;
    }

    public Double getUnloadLongitude() {
        return unloadLongitude;
    }

    public void setUnloadLongitude(Double unloadLongitude) {
        this.unloadLongitude = unloadLongitude;
    }

    public Date getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }

    public Date getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "Haul{" +
                "Uuid='" + uuid + '\'' +
                ", id='" + id + '\'' +
                ", operator='" + operator + '\'' +
                ", equipment='" + equipment + '\'' +
                ", task='" + task + '\'' +
                ", loadLatitude=" + loadLatitude +
                ", loadLongitude=" + loadLongitude +
                ", unloadLatitude=" + unloadLatitude +
                ", unloadLongitude=" + unloadLongitude +
                ", loadTime=" + loadTime +
                ", unloadTime=" + unloadTime +
                ", imei='" + imei + '\'' +
                '}';
    }
}
