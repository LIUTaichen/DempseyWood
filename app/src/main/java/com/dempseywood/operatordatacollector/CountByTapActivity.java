package com.dempseywood.operatordatacollector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.database.db.DB;
import com.dempseywood.operatordatacollector.database.db.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;
import com.dempseywood.operatordatacollector.location.DwLocationListener;
import com.dempseywood.operatordatacollector.rest.HttpRequestTask;

import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;

public class CountByTapActivity extends AppCompatActivity {
    private EquipmentStatusDao equipmentStatusDao;

    private Button loadButton;
    private Button loadMaterialButton;
    private Button unLoadButton;
    private Button unloadMaterialButton;
    private TextView countText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_by_tap);
        initializeViews();
        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new DwLocationListener();
        // Register the listener with the Location Manager to receive location updates

        int permissionCheck = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PermissionChecker.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            Data.setLocation(locationManager.getLastKnownLocation(GPS_PROVIDER));
        }else{
            Log.e("OperatorDetailActivity", "permission for using location service denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            Data.setLocation(locationManager.getLastKnownLocation(GPS_PROVIDER));
        }

        int permissionCheckPhonestack = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        if(permissionCheckPhonestack == PermissionChecker.PERMISSION_GRANTED){
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            Data.setImei(imei);
        }else{
            Log.e("MainActivity", "permission for using phone stack denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);


        }

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity = (CountByTapActivity) v.getContext();
                mainActivity.changeStatus("Loaded");
                mainActivity.changeViewToLoaded();
                showAlert();


            }
        });

        unLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity = (CountByTapActivity) v.getContext();
                mainActivity.changeStatus("Unloaded");
                mainActivity.incrementLoadCount();
                mainActivity.changeViewToUnloaded();
                showAlert();
            }
        });

        loadMaterialButton.setText(Data.getMaterial());
        unloadMaterialButton.setText(Data.getMaterial());
        loadMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity activity = (CountByTapActivity) v.getContext();
                Intent intent = new Intent(activity, ChooseMaterialActivity.class);
                activity.startActivity(intent);
            }
        });
    }



    public void changeViewToLoaded() {
        loadMaterialButton.setVisibility(View.GONE);
        loadButton.setVisibility(View.GONE);
        unLoadButton.setVisibility((View.VISIBLE));
        unloadMaterialButton.setVisibility((View.VISIBLE));

    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if(Data.getStatus().equals("Loaded")){
            builder.setMessage("Haul of " + Data.getMaterial() + " started.");
        }else{
            builder.setMessage("Haul of " + Data.getMaterial() + " completed.");
        }

        final AlertDialog dialog = builder.create();
        dialog.show();

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
    }
    public void incrementLoadCount() {
        Data.setCount(Data.getCount() + 1);
        countText.setText(Data.getCount() + "");


    }

    public void changeViewToUnloaded() {
        loadMaterialButton.setVisibility(View.VISIBLE);
        loadButton.setVisibility(View.VISIBLE);
        unLoadButton.setVisibility((View.GONE));
        unloadMaterialButton.setVisibility(View.GONE);

    }

    public void initializeViews() {
        loadButton = (Button) findViewById(R.id.loadButton);
        loadMaterialButton = (Button) findViewById(R.id.loadMaterialButton);
        unLoadButton = (Button) findViewById(R.id.unloadButton);
        unloadMaterialButton = (Button) findViewById(R.id.unloadMaterialButton);
        countText = (TextView) findViewById(R.id.textView);
        countText.setText(Data.getCount() + "");
        if(Data.getStatus().equals("Loaded")){
            changeViewToLoaded();
        }else{
            changeViewToUnloaded();
        }
    }


    private void changeStatus(String statusString) {
        Data.setStatus(statusString);
        final EquipmentStatus equipmentStatus = getEquipmentStatus();
        new HttpRequestTask(getApplicationContext(),equipmentStatus).execute();
    }

    public EquipmentStatus getEquipmentStatus(){
        EquipmentStatus equipmentStatus = new EquipmentStatus();
        equipmentStatus.setStatus(Data.getStatus());
        equipmentStatus.setTask(Data.getMaterial());
        equipmentStatus.setTimestamp(new Date());
        equipmentStatus.setOperator(Data.getOperator());
        equipmentStatus.setEquipment(Data.getEquipment());
        equipmentStatus.setImei(Data.getImei());
        if(Data.getLocation() != null){
            equipmentStatus.setLatitude(Data.getLocation().getLatitude());
            equipmentStatus.setLongitude(Data.getLocation().getLongitude());
        }
        return equipmentStatus;

    }
}
