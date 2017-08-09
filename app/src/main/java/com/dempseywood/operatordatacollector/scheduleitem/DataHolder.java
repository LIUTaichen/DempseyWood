package com.dempseywood.operatordatacollector.scheduleitem;

import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.rest.status.EquipmentStatus;

import java.util.ArrayList;

/**
 * Created by musing on 27/07/2017.
 */

public class DataHolder {




    private EquipmentStatus equipmentStatus = new EquipmentStatus();
    private ArrayList<ScheduleItem> scheduleItemList;
    private Machine machine;
    private Integer[] counts;
   // private



    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public ArrayList<ScheduleItem> getScheduleItemList() {
        return scheduleItemList;
    }

    public void setScheduleItemList(ArrayList<ScheduleItem> scheduleItemList) {
        this.scheduleItemList = scheduleItemList;
    }


    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
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
}
