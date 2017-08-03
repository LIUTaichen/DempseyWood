package com.dempseywood.operatordatacollector.scheduleitem;

import com.dempseywood.operatordatacollector.operatordetail.Machine;

import java.util.ArrayList;

/**
 * Created by musing on 27/07/2017.
 */

public class DataHolder {



    private ArrayList<ScheduleItem> scheduleItemList;
    private String operatorName;
    private Machine machine;



    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public ArrayList<ScheduleItem> getScheduleItemList() {
        return scheduleItemList;
    }

    public void setScheduleItemList(ArrayList<ScheduleItem> scheduleItemList) {
        this.scheduleItemList = scheduleItemList;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
