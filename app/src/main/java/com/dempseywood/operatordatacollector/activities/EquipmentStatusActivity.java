package com.dempseywood.operatordatacollector.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.listeners.ButtonOnDragListener;
import com.dempseywood.operatordatacollector.listeners.ButtonOnTouchListener;
import com.dempseywood.operatordatacollector.listeners.ScheduleItemSpinnerOnItemSelectedListener;
import com.dempseywood.operatordatacollector.models.DataHolder;

public class EquipmentStatusActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_status);

        TextView operatorName = (TextView)findViewById(R.id.operator_name);
        operatorName.setText(DataHolder.getInstance().getEquipmentStatus().getOperator());
        TextView machinePlateNo = (TextView)findViewById(R.id.machine_name);
        machinePlateNo.setText(DataHolder.getInstance().getEquipment().getName());

        //ArrayList<ScheduleItem> allItems = DataHolder.getInstance().getScheduleItemList();
       /* ArrayList<ScheduleItem> activeItems = new  ArrayList<ScheduleItem>();
        for(ScheduleItem item: allItems){
            if(item.isSelected()){
                activeItems.add(item);
            }
        }*/

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ScheduleItemSpinnerOnItemSelectedListener selectedListener  = new ScheduleItemSpinnerOnItemSelectedListener(this);
        spinner.setOnItemSelectedListener(selectedListener);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schedule_items_array, R.layout.spinner_layout);
        if(DataHolder.getInstance().getCounts() == null) {
            DataHolder.getInstance().setCounts(new Integer[(getResources().getStringArray(R.array.schedule_items_array).length)]);
            for (int i = 0; i < DataHolder.getInstance().getCounts().length; i++) {
                DataHolder.getInstance().getCounts()[i] = 0;
            }
        }

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.task_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Button buttonUnloaded = (Button) findViewById(R.id.unloadedButton);
        Button buttonLoaded = (Button) findViewById(R.id.loadedButton);

        buttonLoaded.setClickable(false);
        buttonLoaded.setBackgroundResource(R.drawable.inactive);
        buttonUnloaded.setOnTouchListener(new ButtonOnTouchListener());
        buttonLoaded.setOnTouchListener(new ButtonOnTouchListener());
        buttonUnloaded.setOnDragListener(new ButtonOnDragListener());
        buttonLoaded.setOnDragListener(new ButtonOnDragListener());
    }

    public void incrementLoadCount(){
        TextView countView = (TextView)findViewById(R.id.count);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Integer selectedItemPosition = spinner.getSelectedItemPosition();
        DataHolder.getInstance().getCounts()[selectedItemPosition] = DataHolder.getInstance().getCounts()[selectedItemPosition] + 1;

        countView.setText(DataHolder.getInstance().getCounts()[selectedItemPosition].toString());
    }


    public void switchToUnloaded(){
        Button buttonUnloaded = (Button) findViewById(R.id.unloadedButton);
        Button buttonLoaded = (Button) findViewById(R.id.loadedButton);
        buttonUnloaded.setClickable(true);
        buttonLoaded.setClickable(false);
        buttonUnloaded.setBackgroundResource(R.drawable.active_unloaded);
        buttonLoaded.setBackgroundResource(R.drawable.inactive);
        TextView instruction = (TextView)findViewById(R.id.instruction);
        instruction.setText(R.string.drag_down);
        incrementLoadCount();
        DataHolder.getInstance().getEquipmentStatus().setStatus("Unloaded");
    }

    public void switchToLoaded(){
        Button buttonUnloaded = (Button) findViewById(R.id.unloadedButton);
        Button buttonLoaded = (Button) findViewById(R.id.loadedButton);
        buttonUnloaded.setClickable(false);
        buttonLoaded.setClickable(true);
        buttonUnloaded.setBackgroundResource(R.drawable.inactive);
        buttonLoaded.setBackgroundResource(R.drawable.active_loaded);
        TextView instruction = (TextView)findViewById(R.id.instruction);
        instruction.setText(R.string.drag_up);
        DataHolder.getInstance().getEquipmentStatus().setStatus("Loaded");
    }


}
