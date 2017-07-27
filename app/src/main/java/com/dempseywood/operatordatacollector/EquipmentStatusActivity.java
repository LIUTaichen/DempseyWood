package com.dempseywood.operatordatacollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;
import com.dempseywood.operatordatacollector.scheduleitem.ScheduleItem;

import java.util.ArrayList;

public class EquipmentStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_status);

        ArrayList<ScheduleItem> allItems = DataHolder.getInstance().getScheduleItemList();
        ArrayList<ScheduleItem> activeItems = new  ArrayList<ScheduleItem>();
        for(ScheduleItem item: allItems){
            if(item.isSelected()){
                activeItems.add(item);
            }
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

    }
}
