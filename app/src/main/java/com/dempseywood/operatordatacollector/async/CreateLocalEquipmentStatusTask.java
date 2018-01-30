package com.dempseywood.operatordatacollector.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.FinishHaulRequest;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.StartHaulRequest;
import com.dempseywood.operatordatacollector.rest.SynchronizeWithServerTask;
import com.dempseywood.operatordatacollector.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jason.Liu on 19/12/2017.
 */

public class CreateLocalEquipmentStatusTask extends AsyncTask<Void, Void, Haul> {
    private Activity activity;
    private EquipmentStatusDao equipmentStatusDAO;
    private HaulDao haulDao;
    private ObjectMapper mapper = new ObjectMapper();

    public CreateLocalEquipmentStatusTask(Activity activity) {
        this.activity = activity;
        DB.init(activity.getApplicationContext());
        equipmentStatusDAO = DB.getInstance().equipmentStatusDao();
        haulDao = DB.getInstance().haulDao();
    }

    @Override
    protected void onPostExecute(Haul haul) {
        JsonRequest request = null;
        //for new haul
        if (haul.getId() == null) {
            String url = UrlHelper.getStartHaulUrl();
            StartHaulRequest startHaulRequest = new StartHaulRequest();
            startHaulRequest.setUuid(haul.getUuid());
            startHaulRequest.setEquipment(haul.getEquipment());
            startHaulRequest.setImei(haul.getImei());
            startHaulRequest.setLoadLatitude(haul.getLoadLatitude());
            startHaulRequest.setLoadLatitude(haul.getLoadLatitude());
            startHaulRequest.setLoadTime(haul.getLoadTime());
            startHaulRequest.setOperator(haul.getOperator());
            startHaulRequest.setTask(haul.getTask());
            JSONObject jsonObject = null;
            try {

                String json = mapper.writeValueAsString(startHaulRequest);
                Log.i("sending request", json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                Log.e("create", "exception when converting to json", e);
                return;
            }
            request = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Haul haul = null;
                            Log.i("response", response.toString());
                            try {
                                haul = mapper.readValue(response.toString(), Haul.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            AsyncTask task = new AsyncTask<Haul, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Haul... hauls) {
                                    Log.i("test", "1");
                                    int count = hauls.length;
                                    for (int i = 0; i < count; i++) {
                                        Haul localHaul = haulDao.findOneByUuid(hauls[i].getUuid());
                                        localHaul.setId(hauls[i].getId());
                                        haulDao.update(localHaul);
                                        EquipmentStatus loadStatus = equipmentStatusDAO.getStatus("Loaded", hauls[i].getUuid());
                                        loadStatus.setIsSent(true);
                                        equipmentStatusDAO.update(loadStatus);
                                    }
                                    Log.i("create haul", "completed");
                                    return false;
                                }

                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, haul);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() != null) {
                                Log.e("volley", error.getMessage());
                                error.getCause().printStackTrace();
                            }

                            Log.e("volley", "request failed");

                            new SynchronizeWithServerTask(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
                        }
                    });

        } else if (haul.getId() != null) {

            String url = UrlHelper.getFinisheHaulUrl(haul.getId());
            Log.i("url for finishing haul", url);
            FinishHaulRequest finishHaulRequestHaulRequest = new FinishHaulRequest();
            finishHaulRequestHaulRequest.setUnloadLatitude(haul.getUnloadLatitude());
            finishHaulRequestHaulRequest.setUnloadLatitude(haul.getUnloadLatitude());
            finishHaulRequestHaulRequest.setUnloadTime(haul.getUnloadTime());
            JSONObject jsonObject = null;
            try {
                String json = mapper.writeValueAsString(finishHaulRequestHaulRequest);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                Log.e("create", "exception when converting to json", e);
            }
            request = new JsonObjectRequest
                    (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Haul haul = null;
                            try {
                                haul = mapper.readValue(response.toString(), Haul.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            AsyncTask task = new AsyncTask<Haul, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Haul... hauls) {
                                    Log.i("test", "1");
                                    int count = hauls.length;
                                    for (int i = 0; i < count; i++) {
                                        EquipmentStatus loadStatus = equipmentStatusDAO.getStatus("Unloaded", hauls[i].getUuid());
                                        loadStatus.setIsSent(true);
                                        equipmentStatusDAO.update(loadStatus);

                                    }

                                    return false;
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,haul);
                            Log.i("finish haul", "completed");
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("volley", "request failed");
                            new SynchronizeWithServerTask(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;

                        }
                    });

        }

        RequestService.getInstance(activity).addToRequestQueue(request);
        super.onPostExecute(haul);
    }

    @Override
    protected Haul doInBackground(Void... voids) {
        EquipmentStatus latestStatus = equipmentStatusDAO.getLastStatusAfter(DateTimeHelper.getTimeOfStartOfDay(new Date()));
        EquipmentStatus newStatus = getEquipmentStatus();

        Haul haul = null;
        if ("Loaded".equals(newStatus.getStatus())) {
            haul = getHaulFromStatus(newStatus);
            haulDao.save(haul);

        }
        if (latestStatus != null && "Loaded".equals(latestStatus.getStatus())) {
            newStatus.setEquipment(latestStatus.getEquipment());
            newStatus.setOperator(latestStatus.getOperator());
            newStatus.setTask(latestStatus.getTask());
            newStatus.setUuid(latestStatus.getUuid());
            haul = haulDao.findOneByUuid(latestStatus.getUuid());
            haul.setUnloadTime(newStatus.getTimestamp());
            haul.setUnloadLatitude(newStatus.getLatitude());
            haul.setUnloadLongitude(newStatus.getLongitude());
            haulDao.update(haul);
        }
        equipmentStatusDAO.insertAll(newStatus);
        return haul;
    }

    private EquipmentStatus getEquipmentStatus() {
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
        equipmentStatus.setUuid(UUID.randomUUID().toString());
        return equipmentStatus;

    }

    private Haul getHaulFromStatus(EquipmentStatus status) {
        Haul haul = new Haul();
        haul.setEquipment(status.getEquipment());
        haul.setImei(status.getImei());
        haul.setTask(status.getTask());
        haul.setOperator(status.getOperator());
        haul.setUuid(status.getUuid());
        haul.setLoadTime(status.getTimestamp());
        haul.setLoadLatitude(status.getLatitude());
        haul.setLoadLongitude(status.getLongitude());
        return haul;
    }
}
