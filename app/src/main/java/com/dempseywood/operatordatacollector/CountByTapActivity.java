package com.dempseywood.operatordatacollector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.database.db.DB;
import com.dempseywood.operatordatacollector.database.db.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;
import com.dempseywood.operatordatacollector.location.DwLocationListener;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.rest.HttpRequestTask;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.util.Date;

import static android.location.LocationManager.GPS_PROVIDER;

public class CountByTapActivity extends AppCompatActivity {
    private EquipmentStatusDao equipmentStatusDao;

    private Button unloadedButton;
    private Button unloadedMaterialButton;
    private Button loadedButton;
    private Button loadedMaterialButton;
    private TextView countText;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_by_tap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();
        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new DwLocationListener();
        // Register the listener with the Location Manager to receive location updates

        int permissionCheck = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            Location newLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            DataHolder.getInstance().getEquipmentStatus().setLatitude(newLocation.getLatitude());
            DataHolder.getInstance().getEquipmentStatus().setLongitude(newLocation.getLongitude());
        } else {
            Log.e("OperatorDetailActivity", "permission for using location service denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            Location newLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            DataHolder.getInstance().getEquipmentStatus().setLatitude(newLocation.getLatitude());
            DataHolder.getInstance().getEquipmentStatus().setLongitude(newLocation.getLongitude());
        }

        int permissionCheckPhonestack = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheckPhonestack == PermissionChecker.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            DataHolder.getInstance().getEquipmentStatus().setImei(imei);
        } else {
            Log.e("MainActivity", "permission for using phone stack denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);


        }

        unloadedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity = (CountByTapActivity) v.getContext();
                mainActivity.changeStatus("Loaded");
                mainActivity.changeViewToLoaded();
                String message = "Haul of " + DataHolder.getInstance().getEquipmentStatus().getTask() + " started.";
                showStickyAlert(message);


            }
        });

        loadedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity = (CountByTapActivity) v.getContext();
                mainActivity.changeStatus("Unloaded");
                mainActivity.incrementLoadCount();
                mainActivity.changeViewToUnloaded();
                String message = "Haul of " + DataHolder.getInstance().getEquipmentStatus().getTask() + " completed.";
                showStickyAlert(message);
            }
        });
        String currentTask = DataHolder.getInstance().getEquipmentStatus().getTask();
        if (currentTask == null || currentTask.isEmpty()) {
            currentTask = "Top soil";
            DataHolder.getInstance().getEquipmentStatus().setTask(currentTask);
        }
        unloadedMaterialButton.setText(currentTask);
        loadedMaterialButton.setText(currentTask);
        loadedMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(getString(R.string.message_alert_unable_to_change_task));
            }
        });
        unloadedMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity activity = (CountByTapActivity) v.getContext();
                Intent intent = new Intent(activity, ChooseMaterialActivity.class);
                activity.startActivity(intent);
            }
        });
    }


    public void changeViewToLoaded() {
        unloadedMaterialButton.setVisibility(View.GONE);
        unloadedButton.setVisibility(View.GONE);
        loadedButton.setVisibility((View.VISIBLE));
        loadedMaterialButton.setVisibility((View.VISIBLE));

    }

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        TextView title =  new TextView(this);
        title.setText(message);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(40);
        alertDialog.setCustomTitle(title);
        alertDialog.show();
    }

    public void showStickyAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(message);
        builder.setMessage(" This alert will be closed in 5 seconds");
        alertDialog = builder.create();
        TextView title =  new TextView(this);
        title.setText(message);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(40);
        alertDialog.setCustomTitle(title);
        alertDialog.show();

        new CountDownTimer(5100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("This alert will be closed in " + (millisUntilFinished / 1000) + " seconds");
            }
            @Override
            public void onFinish() {
                dismissDialog();
            }
        }.start();
    }

    public void incrementLoadCount() {
        DataHolder.getInstance().setCount(DataHolder.getInstance().getCount() + 1);
        countText.setText(DataHolder.getInstance().getCount() + "");


    }

    public void changeViewToUnloaded() {
        unloadedMaterialButton.setVisibility(View.VISIBLE);
        unloadedButton.setVisibility(View.VISIBLE);
        loadedButton.setVisibility((View.GONE));
        loadedMaterialButton.setVisibility(View.GONE);

    }

    public void initializeViews() {
        unloadedButton = (Button) findViewById(R.id.loadButton);
        unloadedMaterialButton = (Button) findViewById(R.id.loadMaterialButton);
        loadedButton = (Button) findViewById(R.id.unloadButton);
        loadedMaterialButton = (Button) findViewById(R.id.unloadMaterialButton);
        countText = (TextView) findViewById(R.id.textView);
        countText.setText(DataHolder.getInstance().getCount() + "");
        if ("Loaded".equals(DataHolder.getInstance().getEquipmentStatus().getStatus())) {
            changeViewToLoaded();
        } else {
            changeViewToUnloaded();
        }
    }


    private void changeStatus(String statusString) {
        DataHolder.getInstance().getEquipmentStatus().setStatus(statusString);
        final EquipmentStatus equipmentStatus = getEquipmentStatus();
        new HttpRequestTask(getApplicationContext(), equipmentStatus).execute();
    }

    public EquipmentStatus getEquipmentStatus() {
        EquipmentStatus equipmentStatus = new EquipmentStatus();
        equipmentStatus.setStatus(DataHolder.getInstance().getEquipmentStatus().getStatus());
        equipmentStatus.setTask(DataHolder.getInstance().getEquipmentStatus().getTask());
        equipmentStatus.setTimestamp(new Date());
        equipmentStatus.setOperator(DataHolder.getInstance().getEquipmentStatus().getOperator());
        equipmentStatus.setEquipment(DataHolder.getInstance().getEquipment().getName());
        equipmentStatus.setImei(DataHolder.getInstance().getEquipmentStatus().getImei());
        equipmentStatus.setLatitude(DataHolder.getInstance().getEquipmentStatus().getLatitude());
        equipmentStatus.setLongitude(DataHolder.getInstance().getEquipmentStatus().getLongitude());
        equipmentStatus.setIsSent(false);
        return equipmentStatus;

    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();

    }
    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        initializeViews();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, OperatorDetailActivity.class);
                this.startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        Intent intent = new Intent(this, OperatorDetailActivity.class);
        this.startActivity(intent);

        return super.onOptionsItemSelected(item);
    }*/

}
