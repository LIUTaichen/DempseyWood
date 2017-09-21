package com.dempseywood.operatordatacollector.operatordetail.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.choosemachine.activity.ChooseMachineActivity;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.database.dao.MachineDAO;
import com.dempseywood.operatordatacollector.location.DwLocationListener;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.listener.OperatorDetailEventListener;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.location.LocationManager.*;

public class OperatorDetailActivity extends AppCompatActivity {
    private DbHelper mDbHelper;
    private MachineDAO machineDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_detail);
        mDbHelper = new DbHelper(this);
        machineDAO = new MachineDAO(mDbHelper);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new DwLocationListener();
        // Register the listener with the Location Manager to receive location updates

        int permissionCheck = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PermissionChecker.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            DataHolder.getInstance().setLocation(locationManager.getLastKnownLocation(GPS_PROVIDER));
        }else{
            Log.e("OperatorDetailActivity", "permission for using location service denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        }




       // Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        prepareData();


        OperatorDetailEventListener listener = new OperatorDetailEventListener();
        //spinner.setOnItemSelectedListener(listener);

        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(listener);

        EditText operatorNameEditText = (EditText) findViewById(R.id.operator_name);

        if(DataHolder.getInstance().getEquipmentStatus().getOperator() != null) {
            operatorNameEditText.append(DataHolder.getInstance().getEquipmentStatus().getOperator());
        }
        Machine selectedMachine = DataHolder.getInstance().getMachine();

        Button machineButton =  (Button) findViewById(R.id.machine_button);
        if (selectedMachine != null) {
           StringBuilder machineButtonTextStringBuilder = new StringBuilder();
            machineButtonTextStringBuilder.append(getString(R.string.machine_name_label));
            machineButtonTextStringBuilder.append(" ");
            machineButtonTextStringBuilder.append(selectedMachine.getPlateNo());
            machineButton.setText(machineButtonTextStringBuilder.toString());
            TextView textView = (TextView)findViewById(R.id.text_machine_description);
            textView.setText(selectedMachine.getDesc());

        }

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


    private void prepareData() {

        machineDAO.removeAll();
        machineDAO.saveMachine("KJF981", "Excavator");

        machineDAO.saveMachine("A78SDF", "Loader");

        machineDAO.saveMachine("HK9F0A", "Dump Truck");

    }


    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("detail activity", "resuming activity");
    }
}
