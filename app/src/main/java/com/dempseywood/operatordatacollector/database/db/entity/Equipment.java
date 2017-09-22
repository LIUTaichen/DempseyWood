package com.dempseywood.operatordatacollector.database.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by musing on 21/09/2017.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Equipment {
    @PrimaryKey
    private Integer id;
    private String name;
    private String fleetId;
    private Double capacity;
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
