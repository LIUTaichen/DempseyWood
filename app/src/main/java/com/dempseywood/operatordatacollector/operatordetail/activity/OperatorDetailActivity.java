package com.dempseywood.operatordatacollector.operatordetail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.choosemachine.activity.ChooseMachineActivity;
import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.operatordetail.listener.OperatorDetailEventListener;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;
import com.google.firebase.iid.FirebaseInstanceId;

public class OperatorDetailActivity extends AppCompatActivity {
    private Button confirmButton;
    private EditText operatorNameEditText;
    private Button machineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_detail);


        OperatorDetailEventListener listener = new OperatorDetailEventListener();
        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(listener);
        operatorNameEditText = (EditText) findViewById(R.id.operator_name);
        machineButton = (Button) findViewById(R.id.machine_button);
        machineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText operatorNameEditText = (EditText) findViewById(R.id.operator_name);
                DataHolder.getInstance().getEquipmentStatus().setOperator(operatorNameEditText.getText().toString());
                OperatorDetailActivity activity = (OperatorDetailActivity) v.getContext();
                Intent intent = new Intent(activity, ChooseMachineActivity.class);
                activity.startActivity(intent);
            }
        });

        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firbase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        if (DataHolder.getInstance().getEquipmentStatus().getOperator() != null) {
            operatorNameEditText.append(DataHolder.getInstance().getEquipmentStatus().getOperator());
        }
        Equipment selectedMachine = DataHolder.getInstance().getEquipment();
        if (selectedMachine != null) {
            StringBuilder machineButtonTextStringBuilder = new StringBuilder();

            machineButtonTextStringBuilder.append(selectedMachine.getName());
            machineButtonTextStringBuilder.append(" ");
            machineButtonTextStringBuilder.append(selectedMachine.getFleetId());
            machineButtonTextStringBuilder.append(" ");
            machineButtonTextStringBuilder.append(selectedMachine.getCategory());

            machineButton.setText(machineButtonTextStringBuilder.toString());

        }

        super.onResume();
        Log.i("detail activity", "resuming activity");
    }
}
