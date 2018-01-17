package com.dempseywood.operatordatacollector.models;
import java.util.Date;

public class FinishHaulRequest {
    private Double unloadLatitude;
    private Double unloadLongitude;
    private Date unloadTime;

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

    public Date getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(Date unloadTime) {
        this.unloadTime = unloadTime;
    }

    @Override
    public String toString() {
        return "FinishHaulRequest{" +
                "unloadLatitude=" + unloadLatitude +
                ", unloadLongitude=" + unloadLongitude +
                ", unloadTime=" + unloadTime +
                '}';
    }
}
