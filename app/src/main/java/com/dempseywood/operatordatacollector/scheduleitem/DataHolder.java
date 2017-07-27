package com.dempseywood.operatordatacollector.scheduleitem;

import java.util.ArrayList;

/**
 * Created by musing on 27/07/2017.
 */

public class DataHolder {



    private ArrayList<ScheduleItem> scheduleItemList;


    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public ArrayList<ScheduleItem> getScheduleItemList() {
        return scheduleItemList;
    }

    public void setScheduleItemList(ArrayList<ScheduleItem> scheduleItemList) {
        this.scheduleItemList = scheduleItemList;
    }
}
