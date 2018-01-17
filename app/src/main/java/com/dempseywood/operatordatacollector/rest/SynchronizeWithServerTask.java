package com.dempseywood.operatordatacollector.rest;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.service.EquipmentStatusJobService;

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
import java.util.List;

/**
 * Created by musing on 04/08/2017.
 */

public class SynchronizeWithServerTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private EquipmentStatusDao equipmentStatusDAO;

    public SynchronizeWithServerTask(Context context) {
        Log.i("SynchronizeWithServer", "started");
        this.context = context;
        DB.init(this.context);
        equipmentStatusDAO = DB.getInstance().equipmentStatusDao();

    }


    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = false;
        Log.e("SynchronizeWithServer", "doInBackground");
        //final String url = "http://loadcount.ap-southeast-2.elasticbeanstalk.com/api/status";
        final String url = UrlHelper.getStartHaulUrl();
        List<EquipmentStatus> statusList = equipmentStatusDAO.getAllNotSent();
        boolean hasRecordsToBeSent = !statusList.isEmpty();
        if (hasRecordsToBeSent) {
            Log.e("SynchronizeWithServer", "doInBackground - no. of entry to send: " + statusList.size());
        }else{
            Log.i("SynchronizeWithServer", "doInBackground - all records sent to server.");
            return true;
        }
        //stop if no status to be sent
        //TODO: send equipment status according to status
        if (!statusList.isEmpty()) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // set headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));

                HttpEntity<List<EquipmentStatus>> entity = new HttpEntity<List<EquipmentStatus>>(statusList, headers);

// send request and parse result
                ResponseEntity<String> loginResponse = restTemplate
                        .exchange(url, HttpMethod.POST, entity, String.class);
                if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
                    //JSONObject userJson = new JSONObject(loginResponse.getBody());
                    for(EquipmentStatus status : statusList){
                        status.setIsSent(true);
                    }
                    equipmentStatusDAO.updateAll(statusList);

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
