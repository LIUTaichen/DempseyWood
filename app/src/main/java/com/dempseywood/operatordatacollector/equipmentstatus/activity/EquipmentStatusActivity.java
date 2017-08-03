package com.dempseywood.operatordatacollector.equipmentstatus.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.equipmentstatus.listener.ButtonOnDragListener;
import com.dempseywood.operatordatacollector.equipmentstatus.listener.ButtonOnTouchListener;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

public class EquipmentStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_status);

        TextView operatorName = (TextView)findViewById(R.id.operator_name);
        operatorName.setText(DataHolder.getInstance().getOperatorName());
        TextView machinePlateNo = (TextView)findViewById(R.id.machine_name);
        machinePlateNo.setText(DataHolder.getInstance().getMachine().getPlateNo());

        //ArrayList<ScheduleItem> allItems = DataHolder.getInstance().getScheduleItemList();
       /* ArrayList<ScheduleItem> activeItems = new  ArrayList<ScheduleItem>();
        for(ScheduleItem item: allItems){
            if(item.isSelected()){
                activeItems.add(item);
            }
        }*/

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schedule_items_array, R.layout.spinner_layout);


// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.dropdown_layout);
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
        String currentCount = countView.getText().toString();
        int newCount = Integer.parseInt(currentCount);
        countView.setText(newCount + 1 +"");
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
    }
}
