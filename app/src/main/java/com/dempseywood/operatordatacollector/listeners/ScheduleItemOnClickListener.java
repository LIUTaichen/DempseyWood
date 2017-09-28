package com.dempseywood.operatordatacollector.listeners;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.dempseywood.operatordatacollector.models.ScheduleItem;

import java.util.ArrayList;


/**
 * Created by musing on 27/07/2017.
 */

public class ScheduleItemOnClickListener implements AdapterView.OnItemClickListener {

    private ArrayList<ScheduleItem> itemList;
    private Button button;

    public ScheduleItemOnClickListener(ArrayList<ScheduleItem> itemList, Button button){
        this.itemList = itemList;
        this.button = button;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

       /* TextView textView = (TextView) parent.findViewById(R.id.listText);
        Toast.makeText(view.getContext(),
                textView.getText(), Toast.LENGTH_SHORT).show();
        // color selection select item*/
        ScheduleItem item = itemList.get(position);
        item.setSelected(!item.isSelected());
        view.setBackgroundColor(item.isSelected() ? Color.rgb(27, 179, 245) : Color.WHITE);


        button.setEnabled(false);
        button.setBackgroundColor(Color.LTGRAY);
        for(ScheduleItem scheduleItem: itemList){
            if(scheduleItem.isSelected()){
                button.setEnabled(true);
                button.setBackgroundColor(Color.BLUE);
            }
        }


    }

    public void setItemList(ArrayList<ScheduleItem> itemList){
        this.itemList = itemList;
    }

    public void setButton(Button button){
        this.button = button;
    }
}
