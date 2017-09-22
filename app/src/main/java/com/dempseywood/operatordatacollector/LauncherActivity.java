package com.dempseywood.operatordatacollector;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dempseywood.operatordatacollector.database.db.DB;
import com.dempseywood.operatordatacollector.database.db.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.database.db.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.database.db.entity.EquipmentStatus;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
    private EquipmentStatusDao equipmentStatusDao;
    private EquipmentDao equipmentDao;
    private String tag = "Launcher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
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

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<Equipment[]> responseEntity = restTemplate.getForEntity(url, Equipment[].class);
                Log.d(tag, "equipments retrieved");
                Log.d(tag, responseEntity.getBody().length + " size ");
                equipmentDao.deleteAll();
                equipmentDao.insertAll(responseEntity.getBody());

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
                    Calendar calendar = Calendar.getInstance();
                    Date date = new Date();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND ,0);
                    calendar.set(Calendar.MILLISECOND , 0);
                    Date timeOfStartOfToday = calendar.getTime();
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
