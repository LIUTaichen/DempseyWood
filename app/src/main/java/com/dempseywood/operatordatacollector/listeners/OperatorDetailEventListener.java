package com.dempseywood.operatordatacollector.listeners;

import android.content.Intent;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.dempseywood.operatordatacollector.activities.ChooseMachineActivity;
import com.dempseywood.operatordatacollector.activities.CountByTapActivity;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.activities.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.models.DataHolder;

/**
 * Created by musing on 03/08/2017.
 */

public class OperatorDetailEventListener implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @Override
    public void onClick(View v) {


        OperatorDetailActivity  activity = (OperatorDetailActivity) v.getContext();

        EditText operatorName = (EditText) activity.findViewById(R.id.operator_name);
        TextInputLayout inputLayout = (TextInputLayout)activity.findViewById(R.id.operator_name_layout) ;

        Equipment selectedMachine = DataHolder.getInstance().getEquipment();
        if (operatorName.getText().toString().length() == 0) {
            inputLayout.setError(activity.getString(R.string.message_error_operator_name_required));
        }
        else if(selectedMachine == null){
            Snackbar snackbar = Snackbar
                    .make(activity.findViewById(R.id.operator_detail_layout), "Please select a machine", Snackbar.LENGTH_INDEFINITE)
                    .setAction("SELECT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText operatorNameEditText = (EditText) view.getRootView().findViewById(R.id.operator_name);
                            DataHolder.getInstance().getEquipmentStatus().setOperator(operatorNameEditText.getText().toString());
                            Intent intent = new Intent(view.getContext(), ChooseMachineActivity.class);
                            view.getContext().startActivity(intent);

                        }
                    });

            snackbar.show();

        }
        else {

            DataHolder.getInstance().getEquipmentStatus().setOperator(operatorName.getText().toString());

            Intent intent = new Intent(activity, CountByTapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //Intent intent = new Intent(activity, EquipmentStatusActivity.class);
            activity.startActivity(intent);
            activity.finish();
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
