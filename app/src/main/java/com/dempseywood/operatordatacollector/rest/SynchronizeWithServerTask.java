package com.dempseywood.operatordatacollector.rest;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.FinishHaulRequest;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.StartHaulRequest;
import com.dempseywood.operatordatacollector.service.EquipmentStatusJobService;
import com.dempseywood.operatordatacollector.service.RequestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by musing on 04/08/2017.
 */

public class SynchronizeWithServerTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private EquipmentStatusDao equipmentStatusDAO;
    private HaulDao haulDao;

    public SynchronizeWithServerTask(Context context) {
        Log.i("SynchronizeWithServer", "started");
        this.context = context;
        DB.init(this.context);
        equipmentStatusDAO = DB.getInstance().equipmentStatusDao();
        haulDao = DB.getInstance().haulDao();

    }


    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = true;
        Log.e("SynchronizeWithServer", "doInBackground");


        List<EquipmentStatus> statusList = equipmentStatusDAO.getAllNotSent();
        boolean hasRecordsToBeSent = !statusList.isEmpty();
        if (!hasRecordsToBeSent) {
            Log.i("SynchronizeWithServer", "doInBackground - all records sent to server.");
            return true;
        }else{
            Log.e("SynchronizeWithServer", "doInBackground - no. of entry to send: " + statusList.size());

            try {
                final String url = UrlHelper.getBatchUpdateUrl();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                List<String> failedUuids = equipmentStatusDAO.getDistinctFailedUuids();
                List<Haul> toSend = haulDao.findAllWithUuidIn(failedUuids);
                HttpEntity<List<Haul>> entity = new HttpEntity<List<Haul>>(toSend, headers);

// send request and parse result
                ResponseEntity<Haul[]> loginResponse = restTemplate
                        .exchange(url, HttpMethod.POST, entity, Haul[].class);
                if (loginResponse.getStatusCode() == HttpStatus.ACCEPTED) {
                    //JSONObject userJson = new JSONObject(loginResponse.getBody());
                    for(EquipmentStatus status : statusList){
                        status.setIsSent(true);
                    }
                    equipmentStatusDAO.updateAll(statusList);
                    for(Haul receivedHaul : loginResponse.getBody()){
                        Haul localHaul = haulDao.findOneByUuid(receivedHaul.getUuid());
                        if(localHaul != null){
                            localHaul.setId(receivedHaul.getId());
                            haulDao.update(localHaul);
                        }
                    }
                    success =true;
                } else {
                    success =false;
                }
                Log.e("SynchronizeWithServer", "sucessfully finished");
            } catch (Exception e) {
                Log.e("SynchronizeWithServer", e.getMessage(), e);
                success =false;
            }
        }

       return success;

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(!success) {
            Log.i("SynchronizeWithServer", "task failed, scheduling  job");
            JobScheduler jobScheduler =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(new JobInfo.Builder(EquipmentStatusJobService.EquipmentStatusJobId,
                    new ComponentName(context, EquipmentStatusJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
        }
    }

}
