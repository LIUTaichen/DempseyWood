package com.dempseywood.operatordatacollector.equipmentstatus.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.equipmentstatus.activity.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

/**
 * Created by musing on 07/08/2017.
 */

public class ScheduleItemSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private EquipmentStatusActivity activity;

    public ScheduleItemSpinnerOnItemSelectedListener(EquipmentStatusActivity activity){
        this.activity = activity;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView countView = (TextView)activity.findViewById(R.id.count);
        Integer count = DataHolder.getInstance().getCounts()[position];
        countView.setText(count.toString());

        String task = (String) parent.getItemAtPosition(position);
        DataHolder.getInstance().getEquipmentStatus().setTask(task);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
