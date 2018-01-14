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

public class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

    private EquipmentStatus status;
    private Context context;
    private EquipmentStatusDao equipmentStatusDAO;

    public HttpRequestTask(Context context, EquipmentStatus status) {
        Log.i("HttpRequestTask", "started");
        this.status = status;
        if(status != null) {
            Log.i("HttpRequestTask", "status is not null, this is called from app");
        }else {
            Log.i("HttpRequestTask", "status is null, this is called from servicejob");
        }
        this.context = context;
        DB.init(this.context);
        equipmentStatusDAO = DB.getInstance().equipmentStatusDao();

    }


    @Override
    protected Boolean doInBackground(Void... params) {
        boolean success = false;
        Log.e("HttpRequestTask", "doInBackground");
        //final String url = "http://loadcount.ap-southeast-2.elasticbeanstalk.com/api/status";
        if( status != null) {
            equipmentStatusDAO.insertAll(status);
        }
        final String url = context.getString(R.string.web_service) + context.getString(R.string.api_status);
        List<EquipmentStatus> statusList = equipmentStatusDAO.getAllNotSent();
        boolean hasOldRecords = !statusList.isEmpty();
        if (hasOldRecords) {
            Log.e("HttpRequestTask", "doInBackground - no. of entry to send: " + statusList.size());
        }
        //stop if no status to be sent
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
                Log.e("HttpRequestTask", "sucessfully finished");
            } catch (Exception e) {
                Log.e("HttpRequestTask", e.getMessage(), e);
                success =false;
            }
        }

       return success;

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(!success) {
            Log.i("HttpRequestTask", "task failed, scheduling  job");
            JobScheduler jobScheduler =
                    (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(new JobInfo.Builder(EquipmentStatusJobService.EquipmentStatusJobId,
                    new ComponentName(context, EquipmentStatusJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
        }
    }

}
