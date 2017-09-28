package com.dempseywood.operatordatacollector.models;

/**
 * Created by musing on 27/07/2017.
 */

public class ScheduleItem {


    private String itemDescription;
    private boolean isSelected;

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
