package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.listeners.OperatorDetailEventListener;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.google.firebase.iid.FirebaseInstanceId;

public class OperatorDetailActivity extends AppCompatActivity {
    private Button confirmButton;
    private EditText operatorNameEditText;
    private Button machineButton;
    private TextView machineNameTextView;
    private TextView machineFleetIdTextView;
    private TextView machineCategoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        machineNameTextView = (TextView) findViewById(R.id.machine_name_text);
        machineFleetIdTextView = (TextView) findViewById(R.id.machine_fleetId_text);
        machineCategoryTextView = (TextView) findViewById(R.id.machine_category_text);



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
            operatorNameEditText.setText("");
            operatorNameEditText.append(DataHolder.getInstance().getEquipmentStatus().getOperator());
        }

        if(DataHolder.getInstance().getEquipment() != null){
            Equipment equipment = DataHolder.getInstance().getEquipment();
            machineNameTextView.setText(equipment.getName());
            machineFleetIdTextView.setText(equipment.getFleetId());
            machineCategoryTextView.setText(equipment.getCategory());
        }
        Equipment selectedMachine = DataHolder.getInstance().getEquipment();
        if (selectedMachine != null) {

            machineNameTextView.setText(selectedMachine.getName());
            machineFleetIdTextView.setText(selectedMachine.getFleetId());
            machineCategoryTextView.setText(selectedMachine.getCategory());

        }

        super.onResume();
        Log.i("detail activity", "resuming activity");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, CountByTapActivity.class);
                this.startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
