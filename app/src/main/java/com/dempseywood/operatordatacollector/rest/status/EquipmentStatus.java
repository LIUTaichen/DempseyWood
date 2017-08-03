package com.dempseywood.operatordatacollector.rest.status;

import java.util.Date;

/**
 * Created by musing on 04/08/2017.
 */

public class EquipmentStatus {

    private Integer id;

    private String equipment;

    private String operator;

    private String status;

    private Date timestamp;


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
}
