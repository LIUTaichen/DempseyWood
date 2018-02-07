package com.dempseywood.operatordatacollector.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.async.VersionCheckCommand;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.TaskDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.helpers.VersionHelper;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.listeners.DwLocationListener;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;
import com.dempseywood.operatordatacollector.service.RequestService;
import com.dempseywood.operatordatacollector.service.SyncDataService;
import com.dempseywood.operatordatacollector.service.VariableTimeoutRestTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LauncherActivity extends AppCompatActivity implements AsyncActionForDataObserver<Boolean> {
    private EquipmentStatusDao equipmentStatusDao;
    private EquipmentDao equipmentDao;
    private TaskDao taskDao;
    private String tag = "Launcher";
    private static final Integer PERMISSION_REQUEST_CODE = 0;
    private static final int REQUEST_APP_SETTINGS = 168;

    private View mLayout;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private CountDownLatch countDownLatch = new CountDownLatch(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.dempseywood.operatordatacollector.R.layout.activity_launcher);

        mLayout = findViewById(R.id.launcher_layout);
        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();
        equipmentDao =  DB.getInstance().equipmentDao();
        taskDao = DB.getInstance().taskDao();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new DwLocationListener();

        getLocationAndIMEI();
        updatePlants();
        updateTasks();
        checkVersion();
    }

    public void checkVersion(){
        new VersionCheckCommand(this.getApplicationContext(), this).execute();
    }



    public void loadStateFromDatabase(){
        AsyncTask asyncTask = new AsyncTask<Void, Void, Boolean>(){
            public LauncherActivity activity = LauncherActivity.this;

            @Override
            protected Boolean doInBackground(Void... params) {
                DataHolder.getInstance().setEquipments(equipmentDao.getAll());
                EquipmentStatus lastStatus = equipmentStatusDao.getLatestStatus();
                boolean isFirstUse = false;
                //first start up
                if(lastStatus == null){
                    isFirstUse = true;
                    Log.i(tag, "This is first usage, starting operator detail activity");
                }else {

                    DataHolder.getInstance().setEquipmentStatus(lastStatus);
                    Equipment machine = equipmentDao.findByName(lastStatus.getEquipment());
                    if(machine == null){
                        isFirstUse = true;
                        Log.i(tag, "Previously used machine is no longer available, starting operator detail activity");
                    }else {

                        DataHolder.getInstance().setEquipment(machine);
                        Date timeOfStartOfToday = DateTimeHelper.getTimeOfStartOfDay(new Date());
                        if (!lastStatus.getTimestamp().before(timeOfStartOfToday)) {
                            List<EquipmentStatus> statusList = equipmentStatusDao.loadAllAfter(timeOfStartOfToday);
                            Integer count = statusList.size() / 2;
                            DataHolder.getInstance().setCount(count);
                        } else {
                            DataHolder.getInstance().setCount(0);
                            DataHolder.getInstance().getEquipmentStatus().setStatus("Unloaded");
                        }
                        Log.i(tag, "This is not first usage, starting counting activity");
                    }
                }
                return isFirstUse;
            }
            @Override
            protected void onPostExecute(Boolean isFirstUse) {

                activity.launchActivity(isFirstUse);

                super.onPostExecute(isFirstUse);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && requestCode == PERMISSION_REQUEST_CODE) {
            // Permission has been granted.
            boolean allGranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }else{
                    allGranted= true;
                }
            }
            if (allGranted) {
                Log.i(tag,"all permissions permitted, loading location and phone state");
                getLocationAndIMEI();
            } else {
                Log.i(tag,"not all permissions permitted, requesting through snackbar");
                Snackbar snackbar = Snackbar
                        .make(mLayout, R.string.message_require_permissions, Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myAppSettings);
                            }
                        });

                snackbar.show();
            }
        }

    }

    private void getLocationAndIMEI(){

        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasPhonePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
        if(hasLocationPermission && hasPhonePermission){
            locationManager.requestLocationUpdates(GPS_PROVIDER, 60000, 5, locationListener);
            Location newLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if(newLocation == null){
                Log.i(tag, "GPS location not available, requesting network location");
                newLocation = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            }
            if(newLocation != null){
                DataHolder.getInstance().setLocation(newLocation);
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            DataHolder.getInstance().setImei(imei);

        }

        else  {
            requestPermissions();
        }


    }

    private void requestPermissions() {
        Log.e("LauncherActivity", "permission for using location service or phone denied, requesting permission");
        List<String> permissions = new ArrayList<>();
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasPhonePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
        if(!hasLocationPermission || !hasPhonePermission){
            if(!hasLocationPermission){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(!hasPhonePermission){
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            String[] permissionRequests = permissions.toArray(new String[2]);
                    ActivityCompat.requestPermissions(this,
                            permissionRequests,
                            PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            getLocationAndIMEI();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void updatePlants(){
        RequestQueue queue = RequestService.getInstance(this).getRequestQueue();
        final String url = UrlHelper.getFetchEquipmentUrl();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Equipment[] equipments = gson.fromJson(response.toString(), Equipment[].class);
                        new AsyncTask<Equipment, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(Equipment... equipment) {
                                equipmentDao.deleteAll();
                                equipmentDao.insertAll(equipment);
                                Log.d(tag, "plants updated");
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                Toast toast = Toast.makeText(LauncherActivity.this, R.string.message_plants_updated,Toast.LENGTH_LONG);
                                toast.show();

                                super.onPostExecute(aBoolean);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,equipments);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Snackbar snackbar = Snackbar.make(mLayout, R.string.message_plants_update_failure, Snackbar.LENGTH_LONG );
                snackbar.show();
            }
        });
        queue.add(arrayRequest);
    }


    private void updateTasks() {

        RequestQueue queue = RequestService.getInstance(this).getRequestQueue();
        final String url = UrlHelper.getFetchTaskUrl();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Task[] tasks = gson.fromJson(response.toString(), Task[].class);
                        new AsyncTask<Task, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(Task... task) {
                                taskDao.deleteAll();
                                taskDao.insertAll(task);
                                Log.d(tag, "tasks updated");
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                Toast toast = Toast.makeText(LauncherActivity.this, R.string.message_tasks_updated,Toast.LENGTH_LONG);
                                toast.show();
                                super.onPostExecute(aBoolean);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tasks);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Snackbar snackbar = Snackbar.make(mLayout, R.string.message_tasks_update_failure, Snackbar.LENGTH_LONG );
                snackbar.show();
            }
        });
        queue.add(arrayRequest);
    }


    @Override
    public void onPreStart() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {
        loadStateFromDatabase();
    }

    @Override
    public void onTaskCancelled() {

    }

    @Override
    public void receiveData(Boolean isUpdateNeeded) {
        if(isUpdateNeeded){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("A New Version is Available. ");
            builder.setMessage("Please update the app to continue.");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.dempseywood.operatordatacollector")));
                    dialog.dismiss();
                }
            });

           /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    background.start();
                }
            });*/

            builder.setCancelable(false);
            builder.show();
        }else{
            loadStateFromDatabase();
        }
    }
}
