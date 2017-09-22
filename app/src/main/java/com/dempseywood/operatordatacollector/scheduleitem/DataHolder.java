package com.dempseywood.operatordatacollector.scheduleitem;

import android.location.Location;

import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;
import com.dempseywood.operatordatacollector.operatordetail.Machine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 27/07/2017.
 */

public class DataHolder {


    private EquipmentStatus equipmentStatus = new EquipmentStatus();
    private ArrayList<ScheduleItem> scheduleItemList;
    private Equipment equipment;
    private Integer[] counts;
    private Integer count = 0;
    private Location location;
    private List<Equipment> equipments;


    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }

    public ArrayList<ScheduleItem> getScheduleItemList() {
        return scheduleItemList;
    }

    public void setScheduleItemList(ArrayList<ScheduleItem> scheduleItemList) {
        this.scheduleItemList = scheduleItemList;
    }


    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Integer[] getCounts() {
        return counts;
    }

    public void setCounts(Integer[] counts) {
        this.counts = counts;
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }
}
