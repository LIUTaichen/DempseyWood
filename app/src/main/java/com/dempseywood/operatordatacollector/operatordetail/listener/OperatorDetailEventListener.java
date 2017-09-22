package com.dempseywood.operatordatacollector.operatordetail.listener;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.CountByTapActivity;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.equipmentstatus.activity.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

/**
 * Created by musing on 03/08/2017.
 */

public class OperatorDetailEventListener implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @Override
    public void onClick(View v) {


        OperatorDetailActivity activity = (OperatorDetailActivity) v.getContext();

        EditText operatorName = (EditText) activity.findViewById(R.id.operator_name);
        Equipment selectedMachine = DataHolder.getInstance().getEquipment();
        if (operatorName.getText().toString().length() == 0) {
            operatorName.setError("Operator name is required!");
        }
        else if(selectedMachine == null){
            Button machineButton =  (Button) activity.findViewById(R.id.machine_button);
            machineButton.setError("Please select a machine");

        }
        else {

            DataHolder.getInstance().getEquipmentStatus().setOperator(operatorName.getText().toString());

            Intent intent = new Intent(activity, CountByTapActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Equipment machine = (Equipment) parent.getItemAtPosition(position);
        DataHolder.getInstance().setEquipment(machine);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
