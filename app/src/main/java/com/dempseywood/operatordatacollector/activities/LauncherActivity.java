package com.dempseywood.operatordatacollector.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.listeners.DwLocationListener;
import com.dempseywood.operatordatacollector.models.DataHolder;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LauncherActivity extends AppCompatActivity {
    private EquipmentStatusDao equipmentStatusDao;
    private EquipmentDao equipmentDao;
    private String tag = "Launcher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.dempseywood.operatordatacollector.R.layout.activity_launcher);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new DwLocationListener();
        // Register the listener with the Location Manager to receive location updates

        int permissionCheck = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(GPS_PROVIDER
                    , 60000, 10, locationListener);
            Location newLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if(newLocation == null){
                Log.i(tag, "GPS location not available, requesting network location");
                newLocation = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            }
            if(newLocation != null){
                DataHolder.getInstance().getEquipmentStatus().setLatitude(newLocation.getLatitude());
                DataHolder.getInstance().getEquipmentStatus().setLongitude(newLocation.getLongitude());
            }

        } else {
            Log.e("LauncherActivity", "permission for using location service denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 10, locationListener);
            Location newLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if(newLocation == null){
                Log.i(tag, "GPS location not available, requesting network location");
                newLocation = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            }
            if(newLocation != null){
                DataHolder.getInstance().getEquipmentStatus().setLatitude(newLocation.getLatitude());
                DataHolder.getInstance().getEquipmentStatus().setLongitude(newLocation.getLongitude());
            }
        }

        int permissionCheckPhonestack = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE);
        if (permissionCheckPhonestack == PermissionChecker.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            DataHolder.getInstance().getEquipmentStatus().setImei(imei);
        } else {
            Log.e("LauncherActivity", "permission for using phone stack denied, requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    1);


        }


        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();
        equipmentDao =  DB.getInstance().equipmentDao();
        loadStateFromDatabase();
    }

    public void loadStateFromDatabase(){
        AsyncTask asyncTask = new AsyncTask<Void, Void, Boolean>(){
            public LauncherActivity activity = LauncherActivity.this;

            @Override
            protected Boolean doInBackground(Void... params) {
                final String url = activity.getString(R.string.web_service) + activity.getString(R.string.api_equipment);
                try {

                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    ResponseEntity<Equipment[]> responseEntity = restTemplate.getForEntity(url, Equipment[].class);
                    Log.d(tag, "equipments retrieved");
                    Log.d(tag, responseEntity.getBody().length + " size ");
                    equipmentDao.deleteAll();
                    equipmentDao.insertAll(responseEntity.getBody());
                }catch(Exception e){
                    Log.e(tag, "error when communicating with server", e);
                }


                DataHolder.getInstance().setEquipments(equipmentDao.getAll());

                EquipmentStatus lastStatus = equipmentStatusDao.getLatestStatus();


                boolean isFirstUse = false;
                //first start up
                if(lastStatus == null){
                    //TODO: handle the case when first time using the app
                    //TODO: fetch equipment and task from server

                    isFirstUse = true;
                    Log.i(tag, "This is first usage, starting operator detail activity");
                }else {

                    DataHolder.getInstance().setEquipmentStatus(lastStatus);
                    Equipment machine = equipmentDao.findByName(lastStatus.getEquipment());
                    DataHolder.getInstance().setEquipment(machine);
                    Date timeOfStartOfToday = DateTimeHelper.getTimeOfStartOfDay(new Date());
                    if(!lastStatus.getTimestamp().before(timeOfStartOfToday)){
                        List<EquipmentStatus> statusList = equipmentStatusDao.loadAllAfter(timeOfStartOfToday);
                        Integer count = statusList.size() / 2;
                        DataHolder.getInstance().setCount(count);
                    }else{
                        DataHolder.getInstance().setCount(0);
                        DataHolder.getInstance().getEquipmentStatus().setStatus("Unloaded");
                    }
                    Log.i(tag, "This is not first usage, starting counting activity");
                }


                return isFirstUse;
            }

            @Override
            protected void onPostExecute(Boolean isFirstUse) {

                activity.launchActivity(isFirstUse);

                super.onPostExecute(isFirstUse);
            }
        }.execute();

    }

    private void launchActivity(Boolean isFirstUse) {
        Intent intent;
        if(isFirstUse){
            intent = new Intent(this, OperatorDetailActivity.class);
        }else{
             intent = new Intent(this, CountByTapActivity.class);

        }
        this.startActivity(intent);
    }

    public void process(EquipmentStatus equipmentStatus){
        Log.i("Launcher", "yes!");
        DataHolder.getInstance().setEquipmentStatus(equipmentStatus);
        //TODO:pull machine from database
        Equipment machine = equipmentDao.findByName(equipmentStatus.getEquipment());
        DataHolder.getInstance().setEquipment(machine);
        DataHolder.getInstance().setCount(0);
    }



    @Override
    protected void onResume() {
        loadStateFromDatabase();
        super.onResume();
    }
}
