package com.dempseywood.operatordatacollector.operatordetail.activity;

import android.Manifest;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.database.DataCollectorContract;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.database.dao.MachineDAO;
import com.dempseywood.operatordatacollector.location.DwLocationListener;
import com.dempseywood.operatordatacollector.operatordetail.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.listener.OperatorDetailEventListener;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.util.List;

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
            DataHolder.getInstance().setLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }else{
            Log.e("OperatorDetailActivity", "permission for using location service denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        }




        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        prepareData();
        //String[] machines = getMachines();

        List<Machine> machines = machineDAO.findAllMachines();
        //ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.layout.spinner_layout,  machines);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, machines);
// Create an ArrayAdapter using the string array and a default spinner layout
      /*  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schedule_items_array, R.layout.spinner_layout);
*/

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.task_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        OperatorDetailEventListener listener = new OperatorDetailEventListener();
        spinner.setOnItemSelectedListener(listener);

        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(listener);

        EditText operatorNameEditText = (EditText) findViewById(R.id.operator_name);
        operatorNameEditText.setText(DataHolder.getInstance().getEquipmentStatus().getOperator());
        Machine selectedMachine = DataHolder.getInstance().getMachine();
        if (selectedMachine != null) {
            int position = 0;
            for (int i = 0; i < machines.size(); i++) {
                if (machines.get(i).getPlateNo().equals(selectedMachine.getPlateNo())) {
                    position = i;
                }
            }
            spinner.setSelection(position);
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
}
